BEGIN{
	FS=";"
	CONST_CLASS_NAME="CheckPointConstant"
	PACKAGE="mobi.chouette.exchange.validator.checkpoints"
	
}

function createValidatorClass(class, codes)
{
	result = ""
     	result = result "package "PACKAGE";\n"

	result = result  "import mobi.chouette.common.Context;\n"
	result = result  "import mobi.chouette.exchange.validator.ValidateParameters;\n"
	result = result  "import mobi.chouette.model."class";\n\n"

	result = result  "public class "class"Validator extends GenericValidator<"class"> implements "CONST_CLASS_NAME" {\n\n"

	split(codes,codelist,";")
	
	list="";
	for (x in codelist) {
		if (list != ""){
			list=list","
		}
		code=codelist[x]
		constant= labelListByCodeTag[code]
		list=list constant
	}

	result = result  "	private static final String[] codes = {"list"};\n\n"
	result = result  "	@Override\n"
	result = result  "	public void validate(Context context, "class" object, ValidateParameters parameters, String transportMode) {\n"
	result = result  "		super.validate( context,  object,  parameters,transportMode,codes);\n"
	result = result  "	}\n\n"
	for (x in codelist) {
		result = result addValidatorMethod(codelist[x], class)
	}
	
	result = result  "}\n"

	return (result)
}

function addValidatorMethod(code, class){
	code_tag=code	
	gsub(/-/, "", code_tag)

	message=labelListByMessage[code]	
	variable=labelListByVariable[code]	
	prerequis=labelListByPrerequis[code]	
	predicat=labelListByPredicat[code]	
	criticite=labelListByCriticite[code]
	notelabelListByNote[code]	

	res = "\n"
	res = res "	/** \n \
	 * <b>Titre</b> :"label"\n \
	 * <p>\n \
	 * <b>R&eacute;ference Redmine</b> : <a target=\"_blank\" href=\"https://projects.af83.io/issues/"id"\">Cartes #"id"</a>\n \
	 * <p>\n \
	 * <b>Code</b> :"code"\n \
	 * <p>\n \
	 * <b>Variables</b> : "variable"\n \
	 * <p>\n \
	 * <b>Prérequis</b> : "prerequis"\n \
	 * <p>\n \
	 * <b>Prédicat</b> : "predicat"\n \
	 * <p>\n \
	 * <b>Message</b> : "message"\n \
	 * <p>\n \
	 * <b>Criticité</b> : "criticite"\n \
	 * <p>\n \
	 * "note"\n \
	 *\n \
	 * @param context\n \
	 * @return\n \
	 */\n"




	res = res  "	private void check"code_tag"(Context context, "class" object, CheckpointParameters parameters)\n"
	res = res  "	{\n"
	res = res  "	     // TODO 	\n"
	res = res  "	}\n\n"

	return (res)
}

function createConstantClass()
{
	result=""
     	result = result "package "PACKAGE";\n"


	result = result  "public interface "CONST_CLASS_NAME" {\n\n"
	for (code in labelListByCode) {
		result = result  addConstant(code)
	}
	
	result = result  "}"

	return result;

}

function addConstant( code)
{
	label=labelListByCode[code]
	res = ""
	res = res  "   // Check "label"\n"
	res = res  "   public static final String "labelListByCodeTag[code]"=\""code"\";\n\n\n"
	return res
}




NR < 2 { next }
{

	id=$1
	label=$2
	code=$3
	variable=$4
	prerequis=$5
	predicat=$6
	message=$7
	criticite=$8
	note=$9
	class=$10
	if (note != ""){
		note="Note : "$9
	}

	gsub(/ /, "", code)
	gsub(/é/, "e", code)

	CRLF= "<br>\n	 *  "

	gsub(/_CRLF_/,CRLF, message)
	gsub(/_CRLF_/,CRLF, variable)
	gsub(/_CRLF_/,CRLF, predicat)
	
	if (class != ""){
		labelListByCode[code]=label
		code_tag="L"code	
		gsub(/-/, "_", code_tag)
		labelListByCodeTag[code]=code_tag


		labelListByMessage[code]=message	
		labelListByVariable[code]=variable	
		labelListByPrerequis[code]=prerequis	
		labelListByPredicat[code]=predicat	
		labelListByCriticite[code]=criticite	
		labelListByNote[code]=note	

		if (codeListByClass[class] != ""){
			codeListByClass[class]=codeListByClass[class]";"
		}
		codeListByClass[class]=codeListByClass[class]code
	}
} 


END{
	
	packageDir=PACKAGE;

	gsub(/\./, "/", packageDir)
	print "packageDir="packageDir

	packageDir="generated/"packageDir
	
	system("mkdir -p \"" packageDir "\"")
	

	for (class in codeListByClass) {
		res = createValidatorClass(class, codeListByClass[class]) 
		filename= packageDir "/" class"Validator.java"
		print res > filename
	}

	result = createConstantClass()
	print result > packageDir "/CheckPointConstant.java"

}
