package api.services;

import api.models.Link;
import api.models.LinkDTO;
import org.springframework.stereotype.Service;

@Service
public class LinkDTOMapper {
    public Link map(LinkDTO linkDTO) {
        Link link = new Link();
        link.setId(linkDTO.getId());
        link.setName(linkDTO.getName());
        link.setTargetURL(linkDTO.getTargetURL());
        link.setRedirectURL(linkDTO.getRedirectURL());
        link.setPassword(linkDTO.getPassword());
        link.setVisits(linkDTO.getVisits());
        return link;
    }

    public LinkDTO map(Link link) {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(link.getId());
        linkDTO.setName(link.getName());
        linkDTO.setTargetURL(link.getTargetURL());
        linkDTO.setRedirectURL(link.getRedirectURL());
        linkDTO.setPassword(link.getPassword());
        linkDTO.setVisits(link.getVisits());
        return linkDTO;
    }
}
