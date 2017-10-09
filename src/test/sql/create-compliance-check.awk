BEGIN{
	id=0
	FS=";"

	print "insert into compliance_control_sets (id, name, organisation_id, created_at, updated_at) values (1, 'JDC#1', 1, '2017-10-06', '2017-10-06');"
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

	tmp=$3
	split(tmp,tab,"#")
	type=tab[1]
	origin_code=tab[2]
	code=origin_code

	name=type
	gsub(/Control/,"",name)
	gsub(/::/,"-",name)


	minimum=$4
	maximum=$5
	target=$6
	pattern=$7
	criticity=$8

	comment=name " comment"

	control_attributes=""
	if ( minimum != "" ){
		control_attributes=append(control_attributes, "minimum", minimum)
	}

	if ( maximum != "" ){
		control_attributes=append(control_attributes, "maximum", maximum)
	}

	if ( target != "" ){
		control_attributes=append(control_attributes, "target", target)
	}
	
	if ( pattern != "" ){
		control_attributes=append(control_attributes, "pattern", pattern)
	}


	if (control_attributes == ""){
		control_attributes="NULL";
	}
	else{
		control_attributes="'"control_attributes"'"
	}

	id++

	insert="INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES";
	#insert="INSERT INTO ..."	
	values="("id", "jdcId", '"type"', "control_attributes", '"name"', '"code"', '"criticity"', '"comment"', '2017-10-04 12:13:30.801847', '2017-10-04 12:13:30.801847', '"origin_code"', NULL);"; 

	print insert values

}

END{
}
