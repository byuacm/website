all: init

init: scp init.sql

.PHONY: scp
scp:
	scp acm@acm.byu.edu:revamp/* ./
	mv production.conf conf/production.conf

.PHONY: init.sql
init.sql:
	mysql -u root --verbose < init.sql

