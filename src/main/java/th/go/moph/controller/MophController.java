package th.go.moph.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MophController {

	@GetMapping("/api/moph/00/00")
	public String api00() {
		return "this is api 00 00";
	}
}
