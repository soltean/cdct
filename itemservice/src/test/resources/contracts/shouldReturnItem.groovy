package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/items/A'
        headers {
            header('Content-Type': consumer(regex('application/json')))
        }
    }
    response {
        status 302
        body(
                    code    : 'A',
                    reservePrice   : value(producer(regex('[0-9]+')))
        )
        headers {
            header('Content-Type': value(
                    producer('application/json;charset=UTF-8'),
                    consumer('application/json;charset=UTF-8'))
            )
        }
    }
}