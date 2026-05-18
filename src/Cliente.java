public class Cliente {
    
    public int id;

    public String nome;

    public String email;

    public String telefone;


    public Cliente(int id, String nome, String email, String telefone) {

        this.id = id;

        this.nome = nome;

        this.email = email;

        this.telefone = telefone;

    }


    @Override

    public String toString() {

        return nome;

    }
}
