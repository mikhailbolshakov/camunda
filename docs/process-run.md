If all steps have been successfully completed you can browse a camunda web app on ``http://localhost:8080/camunda``

Then you can run a test process by sending POST request with the parameters

[http://localhost:8080/camunda/custom-api/process/instance](http://localhost:8080/camunda/custom-api/process/instance)

````json
{
	"processKey": "auto.mobile-service.platform.process",
	"variables": {
		"clientUserId": "client1"	
	}
}
````
 
as a result you should get the following response

````json
{
    "processInstanceId": "23",
    "rootBusinessKey": "8d9cc239-ee24-48ad-ad58-50c6a6bcf62e"
}
````

then you can check the process state by the executing the query

````sql
select  p.ID_,
		p.PROC_DEF_KEY_,
		p.STATE_,
		t.NAME_ as task_name,
		CONCAT(usr.FIRST_, ' ', usr.LAST_) assignee,
		grp.NAME_,
		case when t.END_TIME_ is null then 'in progress' else 'finished' end status, 
		t.DURATION_ 
  from ACT_HI_PROCINST p
  	left join ACT_HI_TASKINST t on p.ID_  = t.PROC_INST_ID_ 
  	left join ACT_ID_USER  usr on t.ASSIGNEE_  = usr.ID_ 
  	left join ACT_ID_MEMBERSHIP  ms on usr.ID_ = ms.USER_ID_ 
  	left join ACT_ID_GROUP  grp on ms.GROUP_ID_ = grp.ID_
  	where p.BUSINESS_KEY_  = [business-key]
  	order by t.START_TIME_ 
````
