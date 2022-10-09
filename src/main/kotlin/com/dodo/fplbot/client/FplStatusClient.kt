package com.dodo.fplbot.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(value = "fplStatusClient", url = "\${fpl.api.status-url}")
interface FplStatusClient {
    @RequestMapping(method = [RequestMethod.GET])
    fun getStatus(): GameDto?
}
