package com.appsmith.server.solutions;

import com.appsmith.server.domains.Application;
import com.appsmith.server.domains.DefaultResources;
import com.appsmith.server.dtos.ActionDTO;
import com.appsmith.server.dtos.ActionViewDTO;
import com.appsmith.server.dtos.ApplicationPagesDTO;
import com.appsmith.server.dtos.LayoutDTO;
import com.appsmith.server.dtos.PageDTO;
import com.appsmith.server.dtos.PageNameIdDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SanitiseResponse {

    public PageDTO sanitisePageDTO(PageDTO page) {
        DefaultResources defaults = page.getDefaultResources();
        page.setApplicationId(defaults.getDefaultApplicationId());
        page.setId(defaults.getDefaultPageId());
        return page;
    }

    public ApplicationPagesDTO sanitiseApplicationPagesDTO(ApplicationPagesDTO applicationPages) {
        List<PageNameIdDTO> pageNameIdList = applicationPages.getPages();
        pageNameIdList.forEach(page -> page.setId(page.getGitDefaultPageId()));
        return applicationPages;
    }

    public ActionDTO sanitiseActionDTO(ActionDTO action) {
        DefaultResources defaults = action.getDefaultResources();
        action.setApplicationId(defaults.getDefaultApplicationId());
        action.setPageId(defaults.getDefaultPageId());
        action.setId(defaults.getDefaultActionId());
        return action;
    }

    public LayoutDTO sanitiseLayoutDTO(LayoutDTO layout) {
        layout.getActionUpdates()
                .forEach(updateLayoutAction -> updateLayoutAction.setId(updateLayoutAction.getDefaultActionId()));

        layout.getLayoutOnLoadActions().forEach(layoutOnLoadAction ->
                layoutOnLoadAction.forEach(onLoadAction -> onLoadAction.setId(onLoadAction.getDefaultActionId())));
        return layout;
    }

    public ActionViewDTO sanitiseActionViewDTO(ActionViewDTO viewDTO) {
        DefaultResources defaults = viewDTO.getDefaultResources();
        viewDTO.setId(defaults.getDefaultActionId());
        viewDTO.setPageId(defaults.getDefaultPageId());
        return viewDTO;
    }

    public Application sanitiseApplication(Application application) {
        if (application.getGitApplicationMetadata() != null
                && !StringUtils.isEmpty(application.getGitApplicationMetadata().getDefaultApplicationId())) {
            application.setId(application.getGitApplicationMetadata().getDefaultApplicationId());
        }
        application
                .getPages()
                .forEach(page -> {
                    if (!StringUtils.isEmpty(page.getDefaultPageId())) {
                        page.setId(page.getDefaultPageId());
                    }
                });
        return application;
    }
}