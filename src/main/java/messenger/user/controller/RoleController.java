package messenger.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import messenger.user.dto.RoleRequestDto;
import messenger.user.dto.RoleResponseDto;
import messenger.user.service.RoleService;
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
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRole(@RequestBody @Valid RoleRequestDto dto, HttpServletRequest request) {
        log.info("Received request to create new role");

        return roleService.createRole(dto);
    }

    @GetMapping("/{name}")
    public RoleResponseDto findRoleByName(@PathVariable String name) {
        return roleService.findRoleByName(name);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> findAllRoles() {
        List<RoleResponseDto> payload = roleService.findAllRoles();

        return new ResponseEntity<>(payload,HttpStatus.valueOf(200));
    }

    @DeleteMapping
    public void deleteRoleByName(@RequestParam String name, HttpServletRequest request) {
        log.info("Received request to delete role " + name);

        roleService.deleteRoleByName(name);
    }
}
