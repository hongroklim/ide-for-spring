# bean cafe API
Shopping Service API Specialized in coffee

## Version: 1.0

### Terms of service
about:blank

**Contact information:**  
Hongrok Lim  
https://github.com/rokong/  
hongrr123@gmail.com

**License:** [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)

### /cart

#### GET
##### Summary:

get carts

##### Description:

get user's cart list [userNm]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| cnt | query | product's amount to purchase | No | integer |
| optionCd | query | option cd (empty when only product) | No | string |
| productId | query | product id | No | integer |
| updateDt | query | the last update time | No | string |
| userNm | query | user name | No | string |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Cart](#Cart) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST
##### Summary:

create cart

##### Description:

add product in user's cart [userNm, productId, optionCd, cnt]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| cart | body | cart | Yes | [Cart](#Cart) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Cart](#Cart) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### PUT
##### Summary:

update count

##### Description:

update product's count [userNm, productId, optionCd, cnt]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| cart | body | cart | Yes | [Cart](#Cart) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Cart](#Cart) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete cart

##### Description:

product id or option cd can be used [userNm, productId, optionCd]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| cnt | query | product's amount to purchase | No | integer |
| optionCd | query | option cd (empty when only product) | No | string |
| productId | query | product id | No | integer |
| updateDt | query | the last update time | No | string |
| userNm | query | user name | No | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /category

#### GET
##### Summary:

get category list

##### Description:

get all categories

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Category](#Category) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST
##### Summary:

create category

##### Description:

create category. if ord==0, append last [name, upId, ord]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| category | body | category | Yes | [Category](#Category) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Category](#Category) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /category/{id}

#### GET
##### Summary:

get category

##### Description:

get category by its id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Category](#Category) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete category

##### Description:

delete category which has no children

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /category/{id}/order

#### PUT
##### Summary:

update category's order

##### Description:

change category's order in siblings [id, ord]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| category | body | category | Yes | [Category](#Category) |
| id | path | id | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Category](#Category) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /category/{id}/parent

#### PUT
##### Summary:

update category's parent

##### Description:

change category's parent [id, upId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| category | body | category | Yes | [Category](#Category) |
| id | path | id | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Category](#Category) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /category/{id}/sub

#### GET
##### Summary:

get category's children

##### Description:

get categories which are its children

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Category](#Category) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /delivery/{orderId}

#### GET
##### Summary:

get delivery

##### Description:

get delivery by order id [orderId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| orderId | path | orderId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Delivery](#Delivery) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST
##### Summary:

create delivery

##### Description:

create delivery in specified order id [*]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| delivery | body | delivery | Yes | [Delivery](#Delivery) |
| orderId | path | orderId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Delivery](#Delivery) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### PUT
##### Summary:

update delivery

##### Description:

update delivery [orderId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| delivery | body | delivery | Yes | [Delivery](#Delivery) |
| orderId | path | orderId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Delivery](#Delivery) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete delivery

##### Description:

delete delivery [orderId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| orderId | path | orderId | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /order

#### POST
##### Summary:

create order

##### Description:

create basic order information

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| order | body | order | Yes | [Order](#Order) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Order](#Order) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /order/{id}

#### GET
##### Summary:

get order

##### Description:

get order by order id [id]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Order](#Order) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /order/{id}/pay

#### PUT
##### Summary:

update order pay

##### Description:

update order pay [id, payId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |
| order | body | order | Yes | [Order](#Order) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 201 | Created |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |

### /order/{id}/status

#### PUT
##### Summary:

update order status

##### Description:

update order status [id, orderStatus]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |
| order | body | order | Yes | [Order](#Order) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Order](#Order) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /order/{orderId}/product

#### GET
##### Summary:

get order products

##### Description:

get products by order id [orderId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| orderId | path | orderId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Order Product](#Order-Product) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST
##### Summary:

add order product

##### Description:

add product in order [orderId, productId, (optionCd), cnt, (payId)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| oProduct | body | oProduct | Yes | [Order Product](#Order-Product) |
| orderId | path | orderId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Order Product](#Order-Product) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /order/{orderId}/product/{productId}/{optionCd}

#### GET
##### Summary:

get order product

##### Description:

get order product [orderId, productId, (optionCd)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| optionCd | path | optionCd | Yes | string |
| orderId | path | orderId | Yes | integer |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Order Product](#Order-Product) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### PUT
##### Summary:

update count

##### Description:

update order product's count [orderId, productId, (optionCd), cnt]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| oProduct | body | oProduct | Yes | [Order Product](#Order-Product) |
| optionCd | path | optionCd | Yes | string |
| orderId | path | orderId | Yes | integer |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Order Product](#Order-Product) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete order product

##### Description:

delete order product [orderId, productId, (optionCd)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| optionCd | path | optionCd | Yes | string |
| orderId | path | orderId | Yes | integer |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /pay/api/{orderId}

#### POST
##### Summary:

request payment

##### Description:

prepare payment by requesting API and get redirect URL [orderId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| orderId | path | orderId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | string |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /pay/api/{orderId}/approve

#### GET
##### Summary:

approve payment

##### Description:

approve payment of order by seller [orderId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| orderId | path | orderId | Yes | integer |
| pg_token | query | pg_token | No | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |

#### POST
##### Summary:

approve payment

##### Description:

approve payment of order by seller [orderId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| orderId | path | orderId | Yes | integer |
| pg_token | query | pg_token | No | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 201 | Created |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |

### /pay/api/{orderId}/status

#### GET
##### Summary:

get payment status

##### Description:

get payment status of order in API [orderId]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| orderId | path | orderId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Pay Status](#Pay-Status) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /product

#### GET
##### Summary:

get product list

##### Description:

get product list

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Product](#Product) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST
##### Summary:

create product

##### Description:

create product [name, price, (categoryId), (enabled),sellerNm, (stockCnt), (deliveryId), (deliveryPrice), (discountPrice)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| product | body | product | Yes | [Product](#Product) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product](#Product) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /product/{id}

#### GET
##### Summary:

get product

##### Description:

get product by id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product](#Product) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### PUT
##### Summary:

update product

##### Description:

[(name), (price), (categoryId), (stockCnt), (enabled),(deliveryId), (discountPrice)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |
| product | body | product | Yes | [Product](#Product) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product](#Product) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete product

##### Description:

delete product

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /product/{productId}/detail

#### GET
##### Summary:

get details in product

##### Description:

get detail list in product

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Product Detail](#Product-Detail) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST
##### Summary:

create detail

##### Description:

create product detail [productId, optionCd, (priceChange), (stockCnt), (enabled)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| pDetail | body | pDetail | Yes | [Product Detail](#Product-Detail) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product Detail](#Product-Detail) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /product/{productId}/detail/{optionCd}

#### GET
##### Summary:

get detail

##### Description:

get detail

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| optionCd | path | optionCd | Yes | string |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product Detail](#Product-Detail) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### PUT
##### Summary:

update detail

##### Description:

update product detail [(priceChange), (stockCnt), (enabled)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| optionCd | path | optionCd | Yes | string |
| pDetail | body | pDetail | Yes | [Product Detail](#Product-Detail) |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product Detail](#Product-Detail) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete detail

##### Description:

delete product detail

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| optionCd | path | optionCd | Yes | string |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /product/{productId}/detail/{optionCd}/sub

#### GET
##### Summary:

get details in option

##### Description:

get detail list under option including itself

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| optionCd | path | optionCd | Yes | string |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Product Detail](#Product-Detail) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /product/{productId}/option

#### GET
##### Summary:

get options in product

##### Description:

get product options in product

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Product Option](#Product-Option) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /product/{productId}/option/group

#### POST
##### Summary:

create option group

##### Description:

create product option group [name]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| pOption | body | pOption | Yes | [Product Option](#Product-Option) |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product Option](#Product-Option) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete option group

##### Description:

delete last product option group

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /product/{productId}/option/{groupId}

#### GET
##### Summary:

get options in group

##### Description:

get product options in product and group

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| groupId | path | groupId | Yes | integer |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Product Option](#Product-Option) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST
##### Summary:

create option

##### Description:

create product option []

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| groupId | path | groupId | Yes | integer |
| pOption | body | pOption | Yes | [Product Option](#Product-Option) |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product Option](#Product-Option) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### PUT
##### Summary:

update option group's order

##### Description:

update product option group's order [ord]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| groupId | path | groupId | Yes | integer |
| pOption | body | pOption | Yes | [Product Option](#Product-Option) |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product Option](#Product-Option) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /product/{productId}/option/{groupId}/{optionId}

#### GET
##### Summary:

get option

##### Description:

get product option

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| groupId | path | groupId | Yes | integer |
| optionId | path | optionId | Yes | string |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product Option](#Product-Option) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### PUT
##### Summary:

update option

##### Description:

update option [(name), (ord)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| groupId | path | groupId | Yes | integer |
| optionId | path | optionId | Yes | string |
| pOption | body | pOption | Yes | [Product Option](#Product-Option) |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Product Option](#Product-Option) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete option

##### Description:

delete product option

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| groupId | path | groupId | Yes | integer |
| optionId | path | optionId | Yes | string |
| productId | path | productId | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /review

#### GET
##### Summary:

get reviews

##### Description:

[get reviews (productId), (optionCd), (userNm)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| review | body | review | Yes | [Review](#Review) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [Review](#Review) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /review/

#### POST
##### Summary:

create review

##### Description:

[userNm, orderId, productId, optionCd, rate, content, (isVisible)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| review | body | review | Yes | [Review](#Review) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Review](#Review) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /review/{id}

#### GET
##### Summary:

get review

##### Description:

get review

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Review](#Review) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete review

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

#### PATCH
##### Summary:

update review

##### Description:

[(rate), (content), (isVisible)]

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| review | body | review | Yes | [Review](#Review) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Review](#Review) |
| 204 | No Content |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |

### /user

#### GET
##### Summary:

get user list

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ [User](#User) ] |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST
##### Summary:

create user

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| user | body | user | Yes | [User](#User) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [User](#User) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /user/{userNm}

#### GET
##### Summary:

get user

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| userNm | path | userNm | Yes | string |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [User](#User) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

deleteUser

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| userNm | path | userNm | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /user/{userNm}/authority

#### PUT
##### Summary:

add user authority

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| user | body | user | Yes | [User](#User) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [ string ] |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### DELETE
##### Summary:

delete user authority

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| user | body | user | Yes | [User](#User) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |

### /user/{userNm}/enabled

#### PUT
##### Summary:

update user enabled

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| user | body | user | Yes | [User](#User) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [User](#User) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### /user/{userNm}/pwd

#### PUT
##### Summary:

update user password

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| user | body | user | Yes | [User](#User) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [User](#User) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### Models


#### Cart

Where users put products to purchase

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| userNm | string | user name | No |
| productId | integer | product id | No |
| optionCd | string | option cd (empty when only product) | No |
| cnt | integer | product's amount to purchase | No |
| updateDt | string | the last update time | No |

#### Category

products are classified in this category

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | integer | category id | No |
| name | string | category name | No |
| upId | integer | parent's id | No |
| ord | integer | display order in siblings | No |

#### Delivery

destination of order

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| orderId | integer | order id | No |
| userNm | string | user name | No |
| senderNm | string | sender name | No |
| recipientNm | string | recipient name | No |
| zipCd | integer | zipCode | No |
| address1 | string | main address | No |
| address2 | string | sub address | No |
| contact1 | string | primary contact | No |
| contact2 | string | secondary contact | No |
| message | string | message for delivery | No |

#### Order

Order including primary information

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | integer | order id | No |
| userNm | string | customer name | No |
| price | integer | products' price | No |
| deliveryPrice | integer | delivery price | No |
| payId | integer | pay id | No |
| payNm | string | pay name | No |
| requestDt | string | order request date | No |
| lastEditDt | string | last changed date | No |
| orderStatus | string | order status | No |

#### Order Product

products included in order

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| orderId | integer | order id | No |
| productId | integer | product id | No |
| optionCd | string | option code in Product Detail | No |
| sellerNm | string | seller name | No |
| cnt | integer | product count | No |
| price | integer | product's price | No |
| discountPrice | integer | product's discount price | No |
| orderStatus | string | order status | No |
| deliveryId | integer | delivery id | No |
| productNm | string | product's full name | No |
| optionNm | string | option's full name | No |

#### Pay Status

Payment Status

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| orderId | integer | order id | No |
| payId | integer | pay id | No |
| apiName | string | api name | No |
| apiKey | string | api key | No |
| orderStatus | string | order status | No |
| price | integer | total price | No |
| payMethod | string | pay method | No |

#### Product

Product including primary information

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| stockCntInt | integer |  | No |
| id | integer | product id | No |
| name | string | product name | No |
| price | integer | product price | No |
| categoryId | integer | product's category | No |
| enabled | boolean | is enabled | No |
| sellerNm | string | seller name | No |
| stockCnt | integer | stock amount (can be empty) | No |
| deliveryId | integer | delivery id | No |
| deliveryPrice | integer | delivery price | No |
| discountPrice | integer | discount price | No |

#### Product Detail

detail options of product

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| productId | integer | product id | No |
| optionCd | string | option code | No |
| fullNm | string | option's full name | No |
| priceChange | integer | change price from original | No |
| stockCnt | integer | stock amount | No |
| enabled | boolean | is enabled | No |

#### Product Option

product's option

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| productId | integer | product id | No |
| optionGroup | integer | option group | No |
| optionId | string | option id | No |
| name | string | title or name | No |
| ord | integer | display order in siblings | No |

#### Review

Review from purchased products

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | integer | review id | No |
| userNm | string | writer name | No |
| orderId | integer | order id | No |
| productId | integer | product id | No |
| optionCd | string | option code | No |
| rate | double | rate | No |
| content | string | content | No |
| isVisible | boolean | is visible | No |
| updateDt | string | update date | No |

#### User

users

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| userNm | string | user name | No |
| pwd | string | password | No |
| enabled | boolean | is enabled | No |
| authority | [ string ] | user authorities | No |