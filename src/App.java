import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {


    public static void main(String[] args) throws Exception {
        
        
        








        HttpServer server = HttpServer.create(
            new InetSocketAddress(8080), 0
        );











        /// HOME
        server.createContext("/", exchange -> {
            String html = """
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Sistema de Clientes</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f5f6fa;
                            text-align: center;
                            margin: 0;
                        }

                        header {
                            background-color: #2f3640;
                            color: white;
                            padding: 20px;
                        }

                        h1 {
                            margin: 0;
                        }

                        .container {
                            margin-top: 50px;
                        }

                        .card{
                            display: inline-block;
                            background: white;
                            padding: 30px;
                            margin: 20px;
                            border-radius: 10px;
                            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                            width: 200px;
                        }

                        .card a {
                            display: block;
                            text-decoration: none;
                            color: white;
                            background: #40739e;
                            padding: 10px;
                            border-radius: 5px;
                            margin-top: 15px;
                        }

                        .card a:hover {
                            background: #273c75;
                        }
                    </style>
                </head>
                <body>

                    <header>
                        <h1>Sistema de Clientes</h1>
                        <p>Gestão simples com Java + MySQL</p>
                    </header>

                    <div class="container">

                        <div class="card">
                            <h3>Ver Clientes</h3>
                            <p>Consultar lista completa</p>
                            <a href="/clientes">Abrir</a>
                        </div>

                        <div class="card">
                            <h3>+ Novo Cliente</h3>
                            <p>Adicionar novo registo</p>
                            <a href="/novo">Criar</a>
                        </div>

                    </div>

                    <div class="container">

                        <div class="card">
                            <h3>Ver Produtos</h3>
                            <p>Consultar lista completa</p>
                            <a href="/produtos">Abrir</a>
                        </div>

                        <div class="card">
                            <h3>+ Novo Produto</h3>
                            <p>Adicionar novo registo</p>
                            <a href="/produtonovo">Criar</a>
                        </div>

                    </div>

                </body>
                </html>
            """;

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.getBytes().length);
            exchange.getResponseBody().write(html.getBytes());
            exchange.close();
        });












        //// LISTA
        server.createContext("/clientes", exchange -> {

            StringBuilder html = new StringBuilder();

                html.append("""
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            table { border-collapse: collapse; width: 100%; }
                            th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
                            th { background-color: #f4f4f4; }
                            a { text-decoration: none; margin-right: 10px; }
                        </style>
                    </head>
                    <body>
                    <h2>Lista de Clientes</h2>
                    <a href='/novo'>+ Novo Cliente</a><br><br>

                    <table>
                        <tr>
                            <th>ID</th>
                            <th>Nif</th>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Telefone</th>
                            <th>Ações</th>
                        </tr>
                """);             

            Connection con = LigacaoBD.ligar();

            if (con == null) {
                System.out.println("Erro: ligação falhou!");
                return;
            }   

            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM clientes");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String telefone = rs.getString("telefone");
                    String nifx = rs.getString("nif");

                    html.append("<tr>");
                    html.append("<td>").append(id).append("</td>");
                    html.append("<td>").append(nifx).append("</td>");
                    html.append("<td>").append(nome).append("</td>");
                    html.append("<td>").append(email).append("</td>");
                    html.append("<td>").append(telefone).append("</td>");

                    html.append("<td>");
                    html.append("<a href='/editar?id=").append(id).append("'>Editar</a>");
                    html.append("<a href='/apagar?id=").append(id)
                        .append("' onclick=\"return confirm('Eliminar cliente?')\">Apagar</a>");
                    html.append("</td>");

                    html.append("</tr>");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            html.append("""
                </table>
                </body>
                </html>
            """);

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.toString().getBytes().length);
            exchange.getResponseBody().write(html.toString().getBytes());
            exchange.close();

        });    


















        // FORM NOVO CLIENTE
        server.createContext("/novo", exchange -> {

            StringBuilder html = new StringBuilder();

            html.append("""
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial; }
                        form { width: 300px; }
                        input { width: 100%; padding: 8px; margin-bottom: 10px; }
                        button { padding: 8px 12px; }
                        a { text-decoration: none; }
                    </style>
                </head>
                <body>

                <h2>Novo Cliente</h2>

                <a href='/clientes'>← Voltar à lista</a><br><br>

                <form method='POST' action='/guardar'>
                    NIF:
                    <input name='nif' required>

                    Nome:
                    <input name='nome' required>

                    Email:
                    <input name='email' type='email' required>

                    Telefone:
                    <input name='telefone'>

                    <button type='submit'>Guardar</button>
                </form>

                </body>
                </html>
            """);

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.toString().getBytes().length);
            exchange.getResponseBody().write(html.toString().getBytes());
            exchange.close();
        }); 













       // GUARDAR NOVO CLIENTE
        server.createContext("/guardar", exchange -> {

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            StringBuilder html = new StringBuilder();

            try {
                // Ler body
                String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");

                String[] params = body.split("&");

                String nif = "";
                String nome = "";
                String email = "";
                String telefone = "";

                for (String p : params) {
                    String[] kv = p.split("=");

                    if (kv.length == 2) {
                        String key = kv[0];
                        String value = java.net.URLDecoder.decode(kv[1], "UTF-8");

                        switch (key) {
                            case "nif": nif = value; break;
                            case "nome": nome = value; break;
                            case "email": email = value; break;
                            case "telefone": telefone = value; break;
                        }
                    }
                }

                Connection con = LigacaoBD.ligar();

                if (con == null) {
                    throw new Exception("Ligação à BD falhou!");
                }

                String sql = "INSERT INTO clientes(nome,nif,email,telefone) VALUES (?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, nome);
                ps.setString(2, nif);
                ps.setString(3, email);
                ps.setString(4, telefone);

                ps.executeUpdate();

                ps.close();
                con.close();

                // HTML de sucesso 
                html.append("""
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            body { font-family: Arial; }
                            a { text-decoration: none; }
                        </style>
                    </head>
                    <body>

                    <h2>:-) Cliente guardado com sucesso!</h2>

                    <a href='/clientes'>Ver lista</a><br><br>
                    <a href='/novo'>Inserir novo cliente</a>

                    </body>
                    </html>
                """);

            } catch (Exception e) {
                e.printStackTrace();

                html.append("""
                    <html>
                    <head>
                        <meta charset="UTF-8">
                    </head>
                    <body>

                    <h2>!! Erro ao guardar cliente!</h2>
                    <a href='/novo'>Voltar</a>

                    </body>
                    </html>
                """);
            }

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.toString().getBytes().length);
            exchange.getResponseBody().write(html.toString().getBytes());
            exchange.close();
        });
















        // FORM EDITAR
        server.createContext("/editar", exchange -> {

            StringBuilder html = new StringBuilder();

            try {
                String query = exchange.getRequestURI().getQuery();

                if (query == null || !query.contains("id=")) {
                    throw new Exception("ID inválido");
                }

                int id = Integer.parseInt(query.split("=")[1]);

                Connection con = LigacaoBD.ligar();

                if (con == null) {
                    throw new Exception("Ligação à BD falhou!");
                }

                String sql = "SELECT * FROM clientes WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    throw new Exception("Cliente não encontrado");
                }

                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String telefone = rs.getString("telefone");

                html.append("""
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            body { font-family: Arial; }
                            input { width: 100%; padding: 8px; margin-bottom: 10px; }
                            form { width: 300px; }
                        </style>
                    </head>
                    <body>

                    <h2>Editar Cliente</h2>

                    <a href='/clientes'>« Voltar</a><br><br>

                    <form method='POST' action='/atualizar'>
                """);

                html.append("<input type='hidden' name='id' value='").append(id).append("'>");

                html.append("Nome:<input name='nome' value='").append(nome).append("' required>");
                html.append("Email:<input name='email' value='").append(email).append("' required>");
                html.append("Telefone:<input name='telefone' value='").append(telefone).append("'>");

                html.append("""
                    <button type='submit'>Atualizar</button>
                    </form>

                    </body>
                    </html>
                """);

                rs.close();
                ps.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();

                html.append("""
                    <html>
                    <body>
                    <h2>!Erro ao carregar cliente</h2>
                    <a href='/clientes'>Voltar</a>
                    </body>
                    </html>
                """);
            }

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.toString().getBytes().length);
            exchange.getResponseBody().write(html.toString().getBytes());
            exchange.close();
        }); 

















        // ATUALIZAR CLIENTE 
        server.createContext("/atualizar", exchange -> {

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");

                String[] params = body.split("&");

                String idStr = "";
                String nome = "";
                String email = "";
                String telefone = "";

                for (String p : params) {
                    String[] kv = p.split("=");

                    if (kv.length == 2) {
                        String key = kv[0];
                        String value = java.net.URLDecoder.decode(kv[1], "UTF-8");

                        switch (key) {
                            case "id": idStr = value; break;
                            case "nome": nome = value; break;
                            case "email": email = value; break;
                            case "telefone": telefone = value; break;
                        }
                    }
                }

                int id = Integer.parseInt(idStr);

                Connection con = LigacaoBD.ligar();

                if (con == null) {
                    throw new Exception("Ligação à BD falhou!");
                }

                String sql = "UPDATE clientes SET nome=?, email=?, telefone=? WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, nome);
                ps.setString(2, email);
                ps.setString(3, telefone);
                ps.setInt(4, id);

                ps.executeUpdate();

                ps.close();
                con.close();

                // Redirect (melhor UX)
                exchange.getResponseHeaders().add("Location", "/clientes");
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
                return;

            } catch (Exception e) {
                e.printStackTrace();

                String resp = """
                    <html>
                    <body>
                    <h2>!Erro ao atualizar cliente</h2>
                    <a href='/clientes'>Voltar</a>
                    </body>
                    </html>
                """;

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, resp.getBytes().length);
                exchange.getResponseBody().write(resp.getBytes());
                exchange.close();
            }
        }); 

        









        
        // ELIMINAR CLIENTE
        server.createContext("/apagar", exchange -> {

            StringBuilder html = new StringBuilder();

            try {
                String query = exchange.getRequestURI().getQuery();

                if (query == null || !query.contains("id=")) {
                    throw new Exception("ID inválido");
                }

                int id = Integer.parseInt(query.split("=")[1]);

                Connection con = LigacaoBD.ligar();

                if (con == null) {
                    throw new Exception("Ligação à BD falhou!");
                }

                String sql = "DELETE FROM clientes WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                ps.close();
                con.close();

                html.append("""
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            body { font-family: Arial; }
                            a { text-decoration: none; }
                        </style>
                    </head>
                    <body>
                """);

                if (rows > 0) {
                    html.append("""
                        <h2>Cliente apagado com sucesso!</h2>
                        <a href='/clientes'>Voltar à lista</a>
                    """);
                } else {
                    html.append("""
                        <h2>! Cliente não encontrado!</h2>
                        <a href='/clientes'>Voltar</a>
                    """);
                }

                html.append("""
                    </body>
                    </html>
                """);

            } catch (Exception e) {
                e.printStackTrace();

                html.append("""
                    <html>
                    <head>
                        <meta charset="UTF-8">
                    </head>
                    <body>

                    <h2>!!! Erro ao apagar cliente!</h2>
                    <a href='/clientes'>Voltar</a>

                    </body>
                    </html>
                """);
            }

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.toString().getBytes().length);
            exchange.getResponseBody().write(html.toString().getBytes());
            exchange.close();
        });           
        
      
        









        /**
         * 
         *  nao mexer
         * 
         *  */
        server.start();
        System.out.println("Servidor em http://localhost:8080");


    }
}
