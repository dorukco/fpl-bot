package com.dodo.fplbot.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "fplFixtureClient", url = "\${fpl.api.fixtures-url}")
interface FplFixtureClient {

    @RequestMapping(method = [RequestMethod.GET], params = ["event"])
    fun getContent(@RequestParam("event") event: Int): List<EventDto>?
}