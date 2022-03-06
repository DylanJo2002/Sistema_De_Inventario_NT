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

          document.getElementById("btn_aceptarEdicionProducto")
          .addEventListener('click',editarInformacionProducto);

          document.getElementById("btn_aceptarEliminarProducto")
          .addEventListener('click', eliminarProducto);

          document.getElementById("btn_aceptarCrearProducto")
          .addEventListener('click', crearProducto);
          
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
let registroReferencia;

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
    llenarCategoriaModal($("#editar-categoria-producto")[0]);
    llenarCategoriaModal($("#crear-categoria-producto")[0]);
    return;
  }

}

function crearFragmentoCategorias(noPermitirTodas){
  const fragment = document.createDocumentFragment();
  for (let categoria of categorias) {

    if(categoria.id == 0 || categoria.id == -1){
      if(!noPermitirTodas){
        const child = document.createElement("option");
        child.text = categoria.nombre;
        child.id = categoria.id;
        console.log(child);
        fragment.appendChild(child);
      }
    }else{
      const child = document.createElement("option");
      child.text = categoria.nombre;
      child.id = categoria.id;
      console.log(child);
      fragment.appendChild(child);
    }

  }

  return fragment;
}

function llenarCategorias() {
  const fragment = crearFragmentoCategorias();
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
  agregarEventListener(document.getElementsByClassName("btn-info-product"), 
  llenarInformacionProducto);
  agregarEventListener(document.getElementsByClassName("btn-edit-product"),
  llenarEdicionProducto);
  agregarEventListener(document.getElementsByClassName("btn-delete-product"),
  almacenarReferenciaProducto)
}

//mensajes = [407_Message]
async function doFetch(metodo, recurso, mensajes, estadoOk,json) {

  const settings = {
    method: metodo,
    headers: { Authorization: token, "Access-Control-Allow-Origin": "*" }

  }
  if(json){
    settings.headers["Content-Type"]= "application/json";
    settings.body = JSON.stringify(json);
  }
  console.log(domain + recurso+" :",settings)

  let body;
  try {
    const res = await fetch(domain + recurso, settings);
  
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

function llenarInformacionProducto(ref){
  const referencia_element = $("#mirar-referencia-producto")[0];
  const nombre_element = $("#mirar-nombre-producto")[0];
  const descripcion_element = $("#mirar-descripcion-producto")[0];
  const costo_element = $("#mirar-cu-producto")[0];
  const umbral_element = $("#mirar-umbral-producto")[0];
  const categoria_element = $("#mirar-categoria-producto")[0];

  const producto = productos.find(p=>p.referencia == ref);

  referencia_element.value = producto.referencia;
  nombre_element.value = producto.nombre;
  descripcion_element.innerText = producto.descripcion;
  costo_element.value = producto.costoxunidad;
  umbral_element.value = producto.umbral;
  categoria_element.value = producto.categoriaReference.nombre;

}

function llenarEdicionProducto(ref){
  const referencia_element = $("#editar-referencia-producto")[0];
  const nombre_element = $("#editar-nombre-producto")[0];
  const descripcion_element = $("#editar-descripcion-producto")[0];
  const costo_element = $("#editar-cu-producto")[0];
  const umbral_element = $("#editar-umbral-producto")[0];
  const categoria_element = $("#editar-categoria-producto")[0];

  const producto = productos.find(p=>p.referencia == ref);

  referencia_element.value = producto.referencia;
  nombre_element.value = producto.nombre;
  descripcion_element.value = producto.descripcion;
  costo_element.value = producto.costoxunidad;
  umbral_element.value = producto.umbral;
  categoria_element.value = producto.categoriaReference.nombre;

}

function llenarCategoriaModal(select_node){
  const fragment = crearFragmentoCategorias(true);
  append(select_node,fragment);
}

function agregarEventListener(elementos, funcion){
  const array = Array.from(elementos);
  console.log(`Elementos:`,elementos);
  array.forEach(function (e) {
    const parentArray = e.parentElement.parentElement.getElementsByTagName("td");
    const referencia = parentArray[0].innerText;
    e.addEventListener('click',()=>{
      funcion(referencia);
    });
  });
}

async function editarInformacionProducto(){
  const referencia_element = $("#editar-referencia-producto")[0].value;
  const nombre_element = $("#editar-nombre-producto")[0].value;
  const descripcion_element = $("#editar-descripcion-producto")[0].value;
  const costo_element = $("#editar-cu-producto")[0].value;
  const umbral_element = $("#editar-umbral-producto")[0].value;
  const categoria_element = $("#editar-categoria-producto option:selected").attr("id")

  let body = {
    "nombre": nombre_element,
    "descripcion": descripcion_element,
    "costoxunidad": costo_element ,
    "umbral": umbral_element,
    "categoria": categoria_element
  }

  body = await doFetch("put","products/"+referencia_element,null,200,body);
  if(body){
    alert(`Se editó el producto ${referencia_element} satisfactoriamente.`);
  }
  limpiarCamposEditarProducto();
}

async function eliminarProducto(){

  const body = await doFetch("delete","products/"+registroReferencia,null,200);
  console.log(`Producto elimnado: ${body}`);
  if(body){
    alert(`Se eliminó el producto ${registroReferencia} satisfactoriamente.`);
  }
}


function almacenarReferenciaProducto(ref){
  registroReferencia = ref;
}

async function crearProducto(){
  const referencia_element = $("#crear-referencia-producto")[0].value;
  const nombre_element = $("#crear-nombre-producto")[0].value;
  const descripcion_element = $("#crear-descripcion-producto")[0].value;
  const costo_element = $("#crear-cu-producto")[0].value;
  const umbral_element = $("#crear-umbral-producto")[0].value;
  const categoria_element = $("#crear-categoria-producto option:selected").attr("id");

  let body = {
    "referencia": referencia_element,
    "nombre": nombre_element,
    "descripcion": descripcion_element,
    "costoxunidad": costo_element ,
    "umbral": umbral_element,
    "categoria": categoria_element
  }
  
  body = await doFetch("post","products",null,201,body);
  if(body){
    alert(`El producto ${referencia_element} se creó satisfactoriamente`);
  } 
  $('#agregar').modal('hide');
  limpiarCamposCrearProducto();
}

function limpiarCamposCrearProducto(){
  const referencia_element = $("#crear-referencia-producto")[0].value = '';
  const nombre_element = $("#crear-nombre-producto")[0].value = '';
  const descripcion_element = $("#crear-descripcion-producto")[0].value = '';
  const costo_element = $("#crear-cu-producto")[0].value = '';
  const umbral_element = $("#crear-umbral-producto")[0].value = '';
}

function limpiarCamposEditarProducto(){
  const referencia_element = $("#editar-referencia-producto")[0].value = '';
  const nombre_element = $("#editar-nombre-producto")[0].value = '';
  const descripcion_element = $("#editar-descripcion-producto")[0].value = '';
  const costo_element = $("#editar-cu-producto")[0].value = '';
  const umbral_element = $("#editar-umbral-producto")[0].value = '';
}