#!/usr/bin/env python3.5
import sys
import csv
import os
from redminelib import Redmine


def beginJavaClass():
	javaClass="/**\n" \
	+" *  !!! Generated Code !!!\n" \
	+" *  Do not edit !\n" \
	+" *  Created by create_NetexStifImportFileSetTests.py\n" \
	+" */\n\n"\
	+"package mobi.chouette.exchange.netex_stif.importer;\n"	\
	+"import org.jboss.arquillian.container.test.api.Deployment;\n" \
 	+"import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;\n" \
	+"import org.testng.annotations.Test;\n" \
 	+"public class NetexStifImportFileSetTests extends AbstractNetexStifImportFileSetTests {\n"\
 	+"\n@Deployment\n"\
 	+"	public static EnterpriseArchive createDeployment() {\n"\
 	+"		return AbstractNetexStifImportFileSetTests.createDeployment(NetexStifImportFileSetTests.class);\n"\
 	+"	}\n"
	#print("javaClass=%s" %(javaClass))
	return javaClass


def endJavaClass():
	javaClass="}\n"
	#print("javaClass=%s" %(javaClass))
	return javaClass

def addTest(priority, issueId,testnumber, zipfilename, actionReportStatus, expectedData):
	issue = redmine.issue.get(int(issueId), include='children,journals,watchers')
	group=issueId
	testname=issueId+"-"+testnumber
	description=issue.subject
	
	args=""
	for e in expectedData:
		if len(str(e))>0 :
			if len(args)>0 and not args.endswith(','):
				args += ","
			args+="\""+str(e)+"\""
	if len(args)>0 and not args.startswith(','):
			args = "," + args
	
	test =	"\n	@Test(groups = { \"%s\" }, testName = \"%s\", description = \"%s\", priority=%s)\n \
		public void verifyCard%s_%s() throws Exception {\n  \
			doImport(\"%s\",\"%s\" %s);\n\
		}\n" %(group, testname, description,priority, issueId,testnumber, zipfilename, actionReportStatus, args )

	return test
 
if __name__ == '__main__':
	try:
		myredminekey=os.environ['REDMINE_KEY']
	except:
		myredminekey=""

	if not myredminekey:
		print("Redmine API Key must be set in REDMINE_KEY env")
		sys.exit(1);

	redmine = Redmine('https://projects.af83.io/', key=myredminekey)
	javaClass=beginJavaClass()
	
	
	
	priority=0
	
	with open("expected-result.txt", encoding="utf-8") as f:
		lines = f.readlines()
		for row in lines:
			if row.startswith("#"):
				continue
			fields=row.replace('\n', '').split(";")
			
			filename=fields[0]
			report_status=fields[1]
			expectedData = fields[2:]
			#parse filename
			newfilename = filename.replace("-", "_")
			tmp=newfilename.split('_')
			tmp=tmp[2]
			tmp=tmp.split(".")
			tmp=tmp[0]
			tmp=tmp[-6:]
			issueId=tmp[-4:]
			testnumber=tmp[:2]
			
			priority=priority+1
			result = addTest(priority, issueId, testnumber, filename, report_status,expectedData)
			#print(result)
			javaClass = javaClass + result
		f.close()

	javaClass = javaClass+endJavaClass()
	
	javaFilename="../../java/mobi/chouette/exchange/netex_stif/importer/NetexStifImportFileSetTests.java"
	javaClassFile =  open(javaFilename, "w") 
	javaClassFile.write(javaClass)
	javaClassFile.close()
	print ("%s created successfully !" %(javaFilename))
