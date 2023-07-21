package messenger.user.controller;

import jakarta.validation.Valid;
import messenger.user.dto.RoleRequestDto;
import messenger.user.dto.RoleResponseDto;
import messenger.user.service.interfaces.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("v1/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long createRole(@RequestBody @Valid RoleRequestDto dto) {
        log.info("Received request to create new role {}", dto);

        return roleService.createRole(dto);
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RoleResponseDto findRoleByName(@PathVariable String name) {
        return roleService.findRoleByName(name);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<RoleResponseDto>> findAllRoles() {
        List<RoleResponseDto> payload = roleService.findAllRoles();

        return new ResponseEntity<>(payload,HttpStatus.valueOf(200));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteRoleByName(@RequestParam String name) {
        log.info("Received request to delete role " + name);

        roleService.deleteRoleByName(name);
    }
}
