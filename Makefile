all: integration

clean:
	mvn clean

compile: clean
	mvn package

docker: compile
	docker build -t dapidi/scheduler:0.1 .

integration: docker
	./integration/test.sh

#test: integration
#	python ./integration/test.py

