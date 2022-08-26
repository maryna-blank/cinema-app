package cinema.service.mapper;

import cinema.dto.response.RoleResponseDto;
import cinema.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements ResponseDtoMapper<RoleResponseDto, Role> {
    public RoleResponseDto mapToDto(Role role) {
        RoleResponseDto responseDto = new RoleResponseDto();
        responseDto.setId(role.getId());
        responseDto.setName(role.getRoleName().name());
        return responseDto;
    }
}
