GATEWAY_IMAGE = crm_gateway:latest
USER_IMAGE = crm_user:latest
CUSTOMER_IMAGE = crm_customer:latest

.PHONY: all
all: build-images up

.PHONY: clean-build
clean-build:
	mvn clean install -DskipTests

.PHONY: build-images
build-images: gateway-image user-image customer-image

.PHONY: gateway-image
gateway-image:
	mvn -f ./gateway spring-boot:build-image -Dspring-boot.build-image.imageName=$(GATEWAY_IMAGE) -DskipTests

.PHONY: user-image
user-image:
	mvn -f ./user spring-boot:build-image -Dspring-boot.build-image.imageName=$(USER_IMAGE) -DskipTests

.PHONY: customer-image
customer-image:
	mvn -f ./customer spring-boot:build-image -Dspring-boot.build-image.imageName=$(CUSTOMER_IMAGE) -DskipTests

.PHONY: up
up:
	docker-compose -f compose_all_services.yaml up

.PHONY: down
down:
	docker-compose -f compose_all_services.yaml down

.PHONY: clean-images
clean-images:
	docker rmi -f $(GATEWAY_IMAGE) $(USER_IMAGE) $(CUSTOMER_IMAGE)
