BEGIN{
	id=0
	jdcCount=0
	FS=";"
}
function append(x,a,v){
	result=x	
	if (x != ""){
		result=x","
	}
	result = result "\""a"\" => \""v"\""
	return result;
}
NR>1{

	id=$1
	jdcId=$2
	name=$3
	transport_mode=$4
	transport_submode=$5
	condition_attributes=""



	if ( jdc[jdcId] == "" ){
		jdc[jdcId]=jdcId
		jdcCount++
		print "insert into compliance_control_sets (id, name, organisation_id, created_at, updated_at) values ("jdcId", 'JDC#"jdcCount"', 1, '2017-10-06', '2017-10-06');"
		print "insert into compliance_check_sets (id, name, created_at, updated_at, compliance_control_set_id) values ("jdcId", 'JDC#"jdcCount"', '2017-10-06', '2017-10-06', "jdcId");"
	}
	

	control_attributes=""
	if ( transport_mode != "" ){
		condition_attributes=append(condition_attributes, "transport_mode", transport_mode)
	}

	if ( transport_submode != "" ){
		condition_attributes=append(condition_attributes, "transport_submode", transport_submode)
	}

	
	if (condition_attributes == ""){
		condition_attributes="NULL";
	}
	else{
		condition_attributes="'"condition_attributes"'"
	}

	#insert="INSERT INTO compliance_control_blocks (id, compliance_control_set_id, name, condition_attributes, created_at, updated_at) VALUES";
	##insert="debug INSERT INTO ..."	
	#values="("id", "jdcId", '"name"', '"condition_attributes"', '2017-10-04 12:13:30.801847', '2017-10-04 12:13:30.801847');"; 


	insert="INSERT INTO compliance_check_blocks (id, compliance_check_set_id, name, condition_attributes, created_at, updated_at) VALUES";
	#insert="debug INSERT INTO ..."	
	values="("id", "jdcId", '"name"', "condition_attributes", '2017-10-04 12:13:30.801847', '2017-10-04 12:13:30.801847');"; 

	print insert values

}

END{
}
