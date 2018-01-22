package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url 'items/1'
        headers {
            header('Content-Type': consumer(regex('application/*json*')))
        }
    }
    response {
        status 200
        body(
                    code    : value(producer(regex('[A-Za-z0-9]+'))),
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