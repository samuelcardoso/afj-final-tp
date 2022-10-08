package br.puc.tp_final.track

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/track-ms/rest/track")
class TrackController(
    val trackService: TrackService
) {
    @GetMapping("status/{id}")
    fun status(): String {
        return trackService.status()
    }
}