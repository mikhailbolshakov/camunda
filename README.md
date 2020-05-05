Build

```bash
mvn clean tomcat7:deploy
mvn clean tomcat7:redeploy
```

Start Tomcat
```bash
sudo systemctl start tomcat
```

Check process
```sql
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
  	where p.BUSINESS_KEY_  = 'a4fed6cf-339b-4a83-aaae-dba7a5a717fe'
  	order by t.START_TIME_ 
```

Docker
````
sudo docker image build -t mikhailbolshakov/camunda-tomcat ./ 

sudo docker container run --rm -it --network=host mikhailbolshakov/camunda-tomcat
````