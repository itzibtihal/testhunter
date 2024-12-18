package org.aicha.hunters_leagues.web.vm.mapper;

import org.aicha.hunters_leagues.domain.Hunt;
import org.aicha.hunters_leagues.web.vm.request.HuntRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HuntVmMapper {



    Hunt toHunt(HuntRequest huntRequest);


}

