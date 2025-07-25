package api.controllers;

import api.models.LinkDTO;
import api.models.LinkForm;
import api.services.LinkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(
        path = "/api/links",
        produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE
        }
)
public class LinkController {
    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @Tag(name = "POST", description = "Add new link")
    @PostMapping
    public ResponseEntity saveLink(@RequestBody LinkForm linkForm) {
        LinkDTO linkDTO = linkService.saveLink(linkService.convertFormToDTO(linkForm));
        linkDTO.setPassword("hidden");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", linkDTO.getRedirectURL())
                .body(linkDTO);
    }

    @Tag(name = "GET", description = "Get link information")
    @GetMapping("/{id}")
    public ResponseEntity getLinkInfo(@PathVariable String id) {
        Optional<LinkDTO> optionalLinkDTO = linkService.getLinkByID(id);
        if (optionalLinkDTO.isPresent()) {
            LinkDTO linkDTO = optionalLinkDTO.get();
            linkDTO.setPassword("hidden");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(linkDTO);
        }
        else
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
    }

    @Tag(name = "GET", description = "Redirect")
    @GetMapping("/red/{id}")
    public ResponseEntity redirect(@PathVariable String id) {
        Optional<LinkDTO> optionalLinkDTO = linkService.getLinkByID(id);
        if (optionalLinkDTO.isPresent()) {
            linkService.incrementVisitCountForLinkById(id);
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .body(optionalLinkDTO.get().getTargetURL());
        }
        else
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
    }

    @Tag(name = "PATCH", description = "Update link")
    @PatchMapping("/{id}")
    public ResponseEntity updateLinkById(@PathVariable String id, @RequestBody LinkForm linkForm) {
        Optional<LinkDTO> optionalLinkDTO = linkService.getLinkByID(id);
        if (optionalLinkDTO.isPresent()) {
            String formPassword = linkForm.getPassword();
            String linkPassword = optionalLinkDTO.get().getPassword();
            if (
                linkPassword != null
                && formPassword != null
                && !linkPassword.isEmpty()
                && !formPassword.isEmpty()
                && linkPassword.equals(formPassword)
            ) {
                linkService.updateLinkUsingFormById(id, linkForm);
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            else
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .header("Reason", "Wrong password")
                        .build();
        }
        else
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
    }

    @Tag(name = "DELETE", description = "Delete link")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteLinkById(@PathVariable String id, @RequestHeader String pass) {
        Optional<LinkDTO> optionalLinkDTO = linkService.getLinkByID(id);
        if (optionalLinkDTO.isPresent()) {
            String linkPassword = optionalLinkDTO.get().getPassword();
            if (linkPassword != null && !linkPassword.isEmpty() && !pass.isEmpty() && linkPassword.equals(pass))
                linkService.deleteLinkById(id);
            else
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .header("Reason", "Wrong password")
                        .build();
        }
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
