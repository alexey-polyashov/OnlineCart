package ru.polyan.onlinecart.controllers.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.polyan.onlinecart.utils.StatisticAspectService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/utils/")
public class UtilsApiV1 {

    private final StatisticAspectService sas;

    @GetMapping(value="statistic")
    @ResponseBody
    public ResponseEntity<?> getStatistic(){
        Map<String, String> statsParams = new HashMap<>();
        return new ResponseEntity(sas.getStatistic(), HttpStatus.OK);
    }

}
