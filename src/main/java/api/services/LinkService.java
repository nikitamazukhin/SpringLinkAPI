package api.services;

import api.models.LinkForm;
import api.models.Link;
import api.models.LinkDTO;
import api.repositories.LinkRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LinkService {
    private final LinkDTOMapper linkDTOMapper;
    private final LinkRepository linkRepository;
    private static final String URLPrefix = "http://localhost:8080/api/links/";

    public LinkService(LinkDTOMapper linkDTOMapper, LinkRepository linkRepository) {
        this.linkDTOMapper = linkDTOMapper;
        this.linkRepository = linkRepository;
    }

    private char generateLetter() {
        int random = (int) (Math.random() * 52);
        char baseCapitalization = (random < 26) ? 'A' : 'a';
        return (char) (baseCapitalization + random % 26);
    }

    private String generateID(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++)
            sb.append(generateLetter());
        if (linkRepository.findById(sb.toString()).isEmpty())
            return sb.toString();
        else
            return generateID(len);
    }

    public LinkDTO convertFormToDTO(LinkForm linkForm) {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(generateID(10));
        linkDTO.setName(linkForm.getName());
        linkDTO.setTargetURL(linkForm.getTargetURL());
        linkDTO.setRedirectURL(URLPrefix + linkDTO.getId());
        linkDTO.setPassword(linkForm.getPassword());
        linkDTO.setVisits(0);
        return linkDTO;
    }

    public void incrementVisitCountForLinkById(String id) {
        Optional<Link> linkOptional = linkRepository.findById(id);
        if (linkOptional.isPresent()) {
            Link link = linkOptional.get();
            link.setVisits(link.getVisits() + 1);
            linkRepository.save(link);
        }
    }

    public void updateLinkUsingFormById(String id, LinkForm linkForm) {
        Optional<Link> linkOptional = linkRepository.findById(id);
        if (linkOptional.isPresent()) {
            Link link = linkOptional.get();
            String formName = linkForm.getName();
            String formTargetURL = linkForm.getTargetURL();
            if (formName != null && !formName.isEmpty())
                link.setName(formName);
            if (formTargetURL != null && !formTargetURL.isEmpty())
                link.setTargetURL(formTargetURL);
            linkRepository.save(link);
        }
    }

    public void deleteLinkById(String id) {
        linkRepository.deleteById(id);
    }

    public LinkDTO saveLink(LinkDTO linkDTO) {
        return linkDTOMapper.map(linkRepository.save(linkDTOMapper.map(linkDTO)));
    }

    public Optional<LinkDTO> getLinkByID(String id) {
        return linkRepository.findById(id).map(linkDTOMapper::map);
    }
}
