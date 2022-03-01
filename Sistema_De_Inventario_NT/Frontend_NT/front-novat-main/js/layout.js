$(document).ready(function () {
  document.getElementById("btnInventario").addEventListener("click", (ev) => {
    ev.preventDefault();
    $("#seccion").load("./index.html", () => {
      document.getElementById("productos").addEventListener("click", (ev) => {
        $("#indexContent").load("./productos.html", () => {
          obtenerCategorias(true);
          document
          .getElementById("btn_buscarProducto")
          .addEventListener("click", (ev)=>{
            ev.preventDefault();
            obtenerProductosCallBack();
          });
        });
      });

      document.getElementById("etiquetas").addEventListener("click", (ev) => {
        $("#indexContent").load("./etiquetas.html", () =>
          obtenerCategorias(true)
        );
      });
      document.getElementById("categorias").addEventListener("click", (ev) => {
        $("#indexContent").load("./categorias.html", () =>
          obtenerCategorias(true)
        );
      });
      document.getElementById("inventario").addEventListener("click", (ev) => {
        $("#indexContent").load("./inventario.html", () =>
          obtenerCategorias(true)
        );
      });
    });
  });
});

const domain = "http://localhost:8080/";
const categorias = new Array();
const productos = new Array();
const token = "Bearer " + localStorage.getItem("tokenNT");

async function obtenerProductosCallBack(){
  const referencia = await $("#search_referenciaProducto")[0].value;
  const categoria = await $("#select_categoryProduct option:selected").attr("id") || false;
  if(referencia != 0 && categoria != -1){
    alert('No puede hacer una búsqueda por referencia y categoría al mismo tiempo.');
    return;
  }
  if(referencia == 0 && categoria == -1){
    alert('Debe hacer una búsqueda por referencia o categoría.');
    return;
  }
  if(referencia){
    obtenerProductos(referencia, null);
    return;
  }
  if(categoria){
    obtenerProductos(null, categoria);
    return;
  }
}

async function obtenerCategorias(sinActualizar) {
  if (categorias.length > 0 && sinActualizar) {
    categorias.splice(0,categorias.length);
    llenarCategorias();
    return;
  }
  const body = await doFetch('get','categories',null,200);

  if (body) {
    categorias.push({ nombre: "NINGUNA",id:-1});
    for (let categoria of body.categories) {
      categorias.push(categoria);
    }
    llenarCategorias();
    return;
  }

}

function llenarCategorias() {
  const fragment = document.createDocumentFragment();
  for (let categoria of categorias) {
    const child = document.createElement("option");
    child.text = categoria.nombre;
    child.id = categoria.id;
    console.log(child);
    fragment.appendChild(child);
  }
  const selectProductos = document.getElementById("select_categoryProduct");
  const selectCategorias = document.getElementById("select_categoryCategory");
  const selectEtiquetas = document.getElementById("select_categoryLabel");
  const selectInventario = document.getElementById("select_categoryInventory");

  append(
    selectCategorias || selectProductos || selectEtiquetas || selectInventario,
    fragment
  );
}

function append(root, fragment) {
  root.append(fragment);
}

async function obtenerProductos(referencia, categoria) {
  productos.splice(0,productos.length);
  console.log(await $("#table_products").find("tr:gt(0)"));
  await $("#table_products > tbody").empty();
  let body;
  if (referencia) {
    body = await doFetch('get','products/' + referencia,null,200);
    if(body){
      productos.push(body);
      llenarProductos();
    }
    return;
  }
  if (categoria) {
     body = await doFetch('get','products?categoryId=' +categoria,null,200);
     if(body){
        for(let producto of body.products){
          productos.push(producto);
        }
        llenarProductos();
     }
  }
  return body;
}

async function llenarProductos(){

  const childs = new Array();
  const fragment = document.createDocumentFragment();
  for (let producto of productos) {
    const child = document.createElement('tr');
    const referencia = document.createElement('td');
    const nombre = document.createElement('td');
    const categoria = document.createElement('td');
    const costo = document.createElement('td');

    referencia.textContent = producto.referencia;
    nombre.textContent = producto.nombre;
    costo.textContent = producto.costoxunidad;
    categoria.textContent = producto.categoriaReference.nombre;

    child.appendChild(referencia);
    child.appendChild(nombre);
    child.appendChild(categoria);
    child.appendChild(costo);
    childs.push(child);
    fragment.appendChild(child);
  }
  console.log(fragment);
  try {
    await Promise.all(childs.map(async (child)=> {
      const acciones = await $.get('./accionesProductos.html');
      const newChild = document.createElement('td');
      newChild.innerHTML = acciones;
      child.appendChild(newChild);
    }));
  }catch(err){
    console.log(`Error: ${err.message}`);
  };

  
  const root = await $('#tableBody_productos');
  append(root,fragment);
}



//mensajes = [407_Message]
async function doFetch(metodo, recurso, mensajes, estadoOk) {

  let body;
  try {
    const res = await fetch(domain + recurso, {
      method: metodo,
      headers: { Authorization: token, "Access-Control-Allow-Origin": "*" },
    });
  
    if (res.status == 403) {
      alert("Sesión terminada. Vuelva a iniciar sesión.");
      window.open("../login.html", "_self");
    }
    if (res.status == 407) {
      alert(mensajes[0]);
    }
    if (res.status >= 500) {
      alert("Error interno en el servidor. Contacte a soporte.");
    }
    if(res.status = estadoOk){
      body = await res.json();
      return body;
    } 
    return body;
  }catch(err){
    alert(`Ocurrió el siguiente error: ${err}`);
  }
}
