package messaging.api.user.controller;

import messaging.api.user.dto.RoleRequestDto;
import messaging.api.user.dto.RoleResponseDto;
import messaging.api.user.service.interfaces.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody RoleRequestDto dto) {
        log.info("Received request to create new role {}", dto);

        RoleResponseDto payload = roleService.createRole(dto);

        return new ResponseEntity<>(payload, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{name}")
    public ResponseEntity<RoleResponseDto> findRoleByName(@PathVariable String name) {
        RoleResponseDto payload = roleService.findRoleByName(name);

        return new ResponseEntity<>(payload, HttpStatusCode.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> findAllRoles() {
        List<RoleResponseDto> payload = roleService.findAllRoles();

        return new ResponseEntity<>(payload,HttpStatus.valueOf(200));
    }

    @DeleteMapping
    public void deleteRoleByName(@RequestParam String name) {
        log.info("Received request to delete role " + name);

        roleService.deleteRoleByName(name);
    }

}
