package com.mind.api.domain.sample

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
@Tag(name = "홈")
class HomeController {

    @GetMapping
    fun home() = "home"

}
