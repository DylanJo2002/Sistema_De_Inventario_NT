const domain = "http://localhost:8080/"

function crearSesion(usuario) {
    fetch(domain+"session",{
        method: 'post',
        body: JSON.stringify(usuario),
        headers: {'Content-Type': 'application/json'}   
    }).then((res)=>{
        if(res.status != 201){
            alert('Usuario o contraseña incorrectos');   
            return -1;   
        }
        return res.json();
    }).then((body) => {
        if(body != -1){
            localStorage.setItem('tokenNT',body.token);
            window.open('../layout.html',"_self");
            return;
        }
    });
}

document.getElementById('btnLogin').addEventListener('click',(ev)=>{
    ev.preventDefault();
    const usuario = {
        usuario : $('#txtUser')[0].value,
        clave : $('#txtPass')[0].value
    }
    crearSesion(usuario);
})