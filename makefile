all: init

init: scp init.sql metamodel

.PHONY: scp
scp:
	scp acm@acm.byu.edu:revamp/* ./
	mv production.conf conf/production.conf

.PHONY: init.sql
init.sql:
	mysql -u root --verbose < init.sql

.PHONY: metamodel
metamodel:
	mkdir metamodel

.PHONY: run
run:
	./activator -Dconfig.file=conf/production.conf ~run