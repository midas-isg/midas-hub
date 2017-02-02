package edu.pitt.isg.midas.hub.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.IS_ISG_ADMIN;

@Controller
@SessionAttributes("appUser")
class ServiceController {
    @Autowired
    private ServiceRepository repository;

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String showServices(Model model) {
        model.addAttribute("services", repository.findAll());
        return "secured/home";
    }

    @PostAuthorize(IS_ISG_ADMIN)
    @RequestMapping(value = "/api/services", method = RequestMethod.POST)
    public String postService(@ModelAttribute Service form){
        repository.save(form);
        return "redirect:/services";
    }

    @PostAuthorize(IS_ISG_ADMIN)
    @RequestMapping(value = "/api/services/reload", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity<?> reload(@RequestParam("multipartFile") MultipartFile multipartFile) throws Exception {
        final byte[] bytes = multipartFile.getBytes();
        final Stream<Service> serviceStream = toServiceStream(bytes);
        repository.deleteAll();
        serviceStream.forEach(repository::save);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Stream<Service> toServiceStream(byte[] bytes) throws Exception{
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        ObjectMapper mapper = new CsvMapper();
        MappingIterator<Service> it = mapper.readerFor(Service.class).with(bootstrapSchema).readValues(bytes);
        Iterable<Service> iterable = () -> it;
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}