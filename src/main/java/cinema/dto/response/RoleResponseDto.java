package cinema.dto.response;

public class RoleResponseDto {
    private Long id;
    private String name;

    public RoleResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoleResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
