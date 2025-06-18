package groupf.recipeapp.entity;

public class Region {
    private int id;
    private String name;
    private String code;
    
    //constructor
	public Region(int id,String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Region [id=" + id + ", name=" + name + ", code=" + code + "]";
	}


}    