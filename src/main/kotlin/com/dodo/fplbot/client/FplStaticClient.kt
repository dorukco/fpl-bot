package com.dodo.fplbot.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(value = "fplStaticClient", url = "\${fpl.api.bootstrap-static-url}")
interface FplStaticClient {
    @RequestMapping(method = [RequestMethod.GET])
    fun getContent(): BootstrapDto?
}