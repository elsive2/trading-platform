SERVICE ?= deal-service
TAG ?= latest
NETWORK ?= trading-platform_nexus-net
CONTEXT ?= ./$(SERVICE)

build:
	DOCKER_BUILDKIT=0 docker build --network=$(NETWORK) -t maximyakovlevvv/$(SERVICE):$(TAG) $(CONTEXT)
push:
	docker push maximyakovlevvv/$(SERVICE):$(TAG)

build-and-push: build push