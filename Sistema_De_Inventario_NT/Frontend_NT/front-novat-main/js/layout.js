$(document).ready(function () {
  document.getElementById("btnInventario").addEventListener("click", (ev) => {
    ev.preventDefault();
    $("#seccion").load("./index.html", () => {
      document.getElementById("productos").addEventListener("click", (ev) => {
        $("#indexContent").load("./productos.html", () => {
          obtenerCategorias(true, 1);
          document
            .getElementById("btn_buscarProducto")
            .addEventListener("click", (ev) => {
              ev.preventDefault();
              obtenerProductosCallBack();
            });

          document
            .getElementById("btn_aceptarEdicionProducto")
            .addEventListener("click", editarInformacionProducto);

          document
            .getElementById("btn_aceptarEliminarProducto")
            .addEventListener("click", eliminarProducto);
          document
            .getElementById("btn_aceptarCrearProducto")
            .addEventListener("click", crearProducto);
          document
            .getElementById("btn_cancelarCrearProducto")
            .addEventListener("click", () => {
              limpiarCamposCrearProducto();
              $("#agregar").modal("hide");
            });
          document
            .getElementById("btn_cerrarCrearProducto")
            .addEventListener("click", () => {
              limpiarCamposCrearProducto();
              $("#agregar").modal("hide");
            });
        });
      });

      document.getElementById("etiquetas").addEventListener("click", (ev) => {
        $("#indexContent").load("./etiquetas.html", () => {
          obtenerCategorias(true);
          document
            .getElementById("btn_cerrarCrearEtiqueta")
            .addEventListener("click", () => {
              limpiarCamposCrearEtiqueta();
              $("#modal_agregarEtiqueta").modal("hide");
            });

          document
            .getElementById("btn_cancelarCrearEtiqueta")
            .addEventListener("click", () => {
              limpiarCamposCrearEtiqueta();
              $("#modal_agregarEtiqueta").modal("hide");
            });

          document
            .getElementById("btn_aceptarCrearEtiqueta")
            .addEventListener("click", () => {
              crearEtiqueta();
            });

          document
            .getElementById("btn_buscarEtiqueta")
            .addEventListener("click", (e) => {
              e.preventDefault();
              obtenerEtiquetasCallBack();
            });

          document
            .getElementById("btn_editarEtiqueta")
            .addEventListener("click", editarEtiqueta);

          document
            .getElementById("btn_aceptarEliminarEtiqueta")
            .addEventListener("click", eliminarEtiqueta);
        });
      });
      document.getElementById("categorias").addEventListener("click", (ev) => {
        $("#indexContent").load("./categorias.html", () => {
          obtenerCategorias(true, 2);
          document
            .getElementById("btn_cancelarCrearCategoria")
            .addEventListener("click", () => {
              limpiarCamposCrearCategoria();
              $("#modal_agregarCategoria").modal("hide");
            });
          document
            .getElementById("btn_cerrarCrearCategoria")
            .addEventListener("click", () => {
              limpiarCamposCrearCategoria();
              $("#modal_agregarCategoria").modal("hide");
            });

          document
            .getElementById("btn_aceptarCrearCategoria")
            .addEventListener("click", () => {
              crearCategoria();
            });

          document
            .getElementById("btn_aceptarEditarCategoria")
            .addEventListener("click", () => {
              editarCategoria();
            });

          document
            .getElementById("btn_aceptarEliminarCategoria")
            .addEventListener("click", () => {
              eliminarCategoria();
            });
        });
      });
      document.getElementById("inventario").addEventListener("click", (ev) => {
        $("#indexContent").load("./inventario.html", () => {
          obtenerCategorias(true);

          document
            .getElementById("btn_buscarInventario")
            .addEventListener("click", (e) => {
              e.preventDefault();
              obtenerInventarioCallBack();
            });

          document
            .getElementById("btn_aceptarEditarInventario")
            .addEventListener('click',actualizarInventario)
        });
      });

      document.getElementById("ingresos").addEventListener("click", (ev) => {
        $("#indexContent").load("./ingresos.html", () => {
          obtenerCategorias(true);

          document
          .getElementById("btn_buscarIngreso")
          .addEventListener("click", (ev) => {
            ev.preventDefault();
            obtenerIngresosCallBack();
          });

          document
          .getElementById("btn_aceptarEditarIngreso")
          .addEventListener("click", (ev) => {
            actualizarIngreso();
          });

          
          
        });
      });




    });
  });
});

const domain = "http://localhost:8080/";
const categorias = new Array();
const productos = new Array();
const etiquetas = new Array();
const etiquetasEditadas = new Array();
const inventario = new Array();
const ingresos = new Array();
const token = "Bearer " + localStorage.getItem("tokenNT");
let registroReferencia;
let registroCategoria;
let registroEtiqueta;
let registroIngreso;
const formatter = new Intl.NumberFormat("en-US", {
  style: "currency",
  currency: "USD",
});

async function obtenerProductosCallBack() {
  const referencia = await $("#search_referenciaProducto")[0].value.trim();
  const categoria =
    (await $("#select_categoryProduct option:selected").attr("id")) || false;
  if (referencia != 0 && categoria != -1) {
    alert(
      "No puede hacer una búsqueda por referencia y categoría al mismo tiempo."
    );
    return;
  }
  if (referencia == 0 && categoria == -1) {
    alert("Debe hacer una búsqueda por referencia o categoría.");
    return;
  }
  if (referencia) {
    obtenerProductos(referencia, null);
    return;
  }
  if (categoria) {
    obtenerProductos(null, categoria);
    return;
  }
}

async function obtenerEtiquetasCallBack() {
  const referencia = $("#busqueda-referencia-etiqueta")[0].value.trim();
  const categoria =
    (await $("#select_categoryLabel option:selected").attr("id")) || false;

  if (referencia != 0 && categoria != -1) {
    alert(
      "No puede hacer una búsqueda por referencia y categoría al mismo tiempo."
    );
    return;
  }
  if (referencia == 0 && categoria == -1) {
    alert("Debe hacer una búsqueda por referencia o categoría.");
    return;
  }

  if (referencia) {
    obtenerEtiquetas(referencia);
    return;
  }

  if (categoria) {
    obtenerEtiquetas(null, categoria);
  }
}

async function obtenerInventarioCallBack() {
  const referencia = $("#busqueda-referencia-inventario")[0].value.trim();
  const categoria =
    (await $("#select_categoryInventory option:selected").attr("id")) || false;

  if (referencia != 0 && categoria != -1) {
    alert(
      "No puede hacer una búsqueda por referencia y categoría al mismo tiempo."
    );
    return;
  }
  if (referencia == 0 && categoria == -1) {
    alert("Debe hacer una búsqueda por referencia o categoría.");
    return;
  }

  if (referencia) {
    obtenerInventario(referencia);
    return;
  }

  if (categoria) {
    obtenerInventario(null, categoria);
  }
}

async function obtenerIngresosCallBack() {
  const referencia = $("#search_referenciaIngreso")[0].value.trim();
  const categoria =
    (await $("#select_categoryEntry option:selected").attr("id")) || false;
  const fechaInicio = $("#search_fechaInicioIngreso")[0].value;
  const fechaFin =  $("#search_fechaFinIngreso")[0].value; 
  console.log("FECHA INICIO: "+fechaInicio+" FECHA FIN: "+fechaFin);
  if (referencia != 0 && categoria != -1) {
    alert(
      "No puede hacer una búsqueda por referencia y categoría al mismo tiempo."
    );
    return;
  }
  if (referencia == 0 && categoria == -1) {
    alert("Debe hacer una búsqueda por referencia o categoría.");
    return;
  }

  if((fechaInicio && !fechaFin) || (!fechaInicio && fechaFin)){
    alert("Para realizar una búsqueda por fechas debe proporcionar la fecha de inicio y fin.");
    return;
  }

  if(referencia){
    obtenerIngresos(referencia,null,fechaInicio,fechaFin);

  }else{
    obtenerIngresos(null,categoria,fechaInicio,fechaFin);

  }

}

async function obtenerInventario(referencia, categoria) {
  let recurso;
  if (referencia) {
    recurso = "inventory/reference?reference=" + referencia;
  } else {
    recurso = "inventory/categories?categoryId=" + categoria;
  }

  const body = await doFetch(
    "get",
    recurso,
    [null, `No existe un producto con la referencia ${referencia}`],
    200
  );

  if (body != -1) {
    inventario.splice(0, inventario.length);
    for (item of body.inventario) {
      inventario.push(item);
    }
    llenarInventario();
  }
}

async function obtenerEtiquetas(referencia, categoria) {
  let recurso;
  if (referencia) {
    recurso = "labels/reference?reference=" + referencia;
  } else {
    recurso = "labels/categories?categoryId=" + categoria;
  }

  const body = await doFetch(
    "get",
    recurso,
    [null, `No existe un producto con la referencia ${referencia}`],
    200
  );

  if (body != -1) {
    etiquetas.splice(0, etiquetas.length);
    for (etiqueta of body.labels) {
      etiquetas.push(etiqueta);
    }
    llenarEtiquetas();
  }
}

async function llenarEtiquetas() {
  const root = $("#tableBody_etiquetas")[0];
  const fragment = document.createDocumentFragment();
  await $("#table_labels > tbody").empty();
  const childs = new Array();
  for (let eti of etiquetas) {
    if (eti.nombre != "NINGUNA") {
      const child = document.createElement("tr");
      const id = document.createElement("td");
      const referencia = document.createElement("td");
      const producto = document.createElement("td");
      const etiqueta = document.createElement("td");

      id.innerText = eti.id;
      referencia.innerText = eti.productReference.referencia;
      producto.innerText = eti.productReference.nombre;
      etiqueta.innerText = eti.nombre;

      child.appendChild(id);
      child.appendChild(referencia);
      child.appendChild(producto);
      child.appendChild(etiqueta);
      childs.push(child);
      fragment.appendChild(child);
    }
  }

  try {
    await Promise.all(
      childs.map(async (child) => {
        const acciones = await $.get("./accionesEtiquetas.html");
        const newChild = document.createElement("td");
        newChild.innerHTML = acciones;
        child.appendChild(newChild);
      })
    );
  } catch (err) {
    console.log(`Error: ${err.message}`);
  }

  append(root, fragment);
  agregarEventListener(
    document.getElementsByClassName("btn-edit-label"),
    llenarEdicionEtiqueta
  );
  agregarEventListener(
    document.getElementsByClassName("btn-delete-label"),
    almacenarEtiqueta
  );
}

async function llenarInventario() {
  const root = $("#tableBody_inventario")[0];
  const etiquetasInfo = $("#mostrar-etiqueta-inventario")[0];
  const etiquetasEdit = $("#editar-etiqueta-inventario")[0];
  const cantidadEdit = $("#editar-cantidad-inventario")[0];

  const fragment = document.createDocumentFragment();
  await $("#table_inventory > tbody").empty();
  const childs = new Array();
  for (let item of inventario) {
    const child = document.createElement("tr");
    const referencia = document.createElement("td");
    const producto = document.createElement("td");
    const categoria = document.createElement("td");
    const cantidadTotal = document.createElement("td");
    const numeroEtiquetas = document.createElement("td");

    referencia.innerText = item.referencia;
    producto.innerText = item.producto;
    categoria.innerText = item.categoria;
    cantidadTotal.innerText = item.cantidadTotal;
    numeroEtiquetas.innerText = item.cantidadEtiquetas;

    child.appendChild(referencia);
    child.appendChild(producto);
    child.appendChild(categoria);
    child.appendChild(cantidadTotal);
    child.appendChild(numeroEtiquetas);
    childs.push(child);
    fragment.appendChild(child);
  }

  try {
    await Promise.all(
      childs.map(async (child) => {
        const acciones = await $.get("./accionesInventario.html");
        const newChild = document.createElement("td");
        newChild.innerHTML = acciones;
        child.appendChild(newChild);
      })
    );
  } catch (err) {
    console.log(`Error: ${err.message}`);
  }

  append(root, fragment);

  agregarEventListener(
    document.getElementsByClassName("btn-info-inventory"),
    llenarInformacionInventario
  );
  agregarEventListener(
    document.getElementsByClassName("btn-edit-inventory"),
    llenarEdicionInventario
  );

  etiquetasInfo.addEventListener('click', (ev)=>{
    eventoLlenarCantidadEtiqueta($( "#mostrar-etiqueta-inventario option:selected")
    .attr('id'),"mostrar-referencia-inventario","mostrar-cantidad-inventario");
  });
 
  etiquetasEdit.addEventListener('click', (ev)=>{
      eventoLlenarCantidadEtiqueta($("#editar-etiqueta-inventario option:selected")
      .attr('id'),"editar-referencia-inventario","editar-cantidad-inventario",true);
    });

  cantidadEdit.addEventListener('keydown',(e)=>{
      //const valor = e.target.value;
      if((e.keyCode < 48 || e.keyCode > 57)  && e.keyCode != 46 && e.keyCode != 8
      && e.keyCode != 37 && e.keyCode != 38 && e.keyCode != 39 && e.keyCode != 40){
        e.preventDefault();
      }
      // else {
        
      //   if(valor.length == 1 && (e.keyCode == 8 || e.keyCode == 46)){
      //     cantidadEdit.value = '00';

      //   }
      // }
  })
  cantidadEdit.addEventListener('input', (e)=>{
    const valorString = e.target.value+'';
    let valor = 0;
    if(valorString.length > 0){
      valor = Number.parseInt(valorString);

    }
    const idEtiqueta = $("#editar-etiqueta-inventario option:selected").attr('id');
    const etiquetaEditar = etiquetasEditadas.find(e => e.id==idEtiqueta);
    const inputCantidadTotal = $("#editar-cantidadTotal-inventario")[0];
    const cantidadTotalValor = Number.parseInt(inputCantidadTotal.value);
    inputCantidadTotal.value = cantidadTotalValor-etiquetaEditar.cantidad+valor;
    etiquetaEditar.cantidad = Number.parseInt(valor);

    console.log("EVENTO INPUT, valor actual: "+valor+" ETIQUETA: ",etiquetaEditar);

    //e.target.value=  99;
    // etiquetaEditar.cantidad = valor;
  })
}


/**Seccion 1=productos, 2=categorias */
async function obtenerCategorias(sinActualizar, seccion) {
  if (categorias.length > 0 && sinActualizar) {
    llenarCategorias();
    if (seccion == 1) {
      llenarCategoriaModal($("#editar-categoria-producto")[0]);
      llenarCategoriaModal($("#crear-categoria-producto")[0]);
    }
    if (seccion == 2) {
      llenarTablaCategorias();
    }
    return;
  }

  categorias.splice(0, categorias.length);
  const body = await doFetch("get", "categories", null, 200);

  if (body) {
    categorias.push({ nombre: "NINGUNA", id: -1 });
    for (let categoria of body.categories) {
      categorias.push(categoria);
    }
    llenarCategorias();
    if (seccion == 1) {
      llenarCategoriaModal($("#editar-categoria-producto")[0]);
      llenarCategoriaModal($("#crear-categoria-producto")[0]);
    }
    if (seccion == 2) {
      llenarTablaCategorias();
    }
    return;
  }
}

async function obtenerIngresos(referencia, categoria, fechaInicio, fechaFin){
  let recurso;
  if (referencia) {
    if(fechaInicio && fechaFin){
      recurso = `inventory-entries/${referencia}?dateStart=${fechaInicio}&dateEnd=${fechaFin}`;
    }else {
      recurso = `inventory-entries/${referencia}?dateStart=&dateEnd=`;
    }
    
  } else {
    if(fechaInicio && fechaFin){
      recurso =  `inventory-entries?dateStart=${fechaInicio}&dateEnd=${fechaFin}&categoryId=${categoria}`;
    }else {
      recurso =  `inventory-entries?dateStart=&dateEnd=&categoryId=${categoria}`;
    }
  }

  const body = await doFetch(
    "get",
    recurso,
    [null, `No existe un producto con la referencia ${referencia}`],
    200
  );

  console.log("INGRESOS OBTENIDOS: ",body);

  if (body != -1) {

    ingresos.splice(0, ingresos.length);
    for (item of body.ingresos) {
      ingresos.push(item);
    }
    llenarIngresos();
  }
}

function crearFragmentoCategorias(noPermitirTodas) {
  const fragment = document.createDocumentFragment();
  for (let categoria of categorias) {
    if (categoria.id == 0 || categoria.id == -1) {
      if (!noPermitirTodas) {
        const child = document.createElement("option");
        child.text = categoria.nombre;
        child.id = categoria.id;
        console.log(child);
        fragment.appendChild(child);
      }
    } else {
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
  const selectEtiquetas = document.getElementById("select_categoryLabel");
  const selectInventario = document.getElementById("select_categoryInventory");
  const selectIngresos = document.getElementById("select_categoryEntry");
  if (selectProductos || selectEtiquetas || selectInventario || selectIngresos) {
    append(selectProductos || selectEtiquetas || selectInventario || selectIngresos, fragment);
  }
}

function append(root, fragment) {
  console.log("THIS IS THE ROOT ", root);
  console.log("THIS IS THE fragment ", fragment);
  root.append(fragment);
}

async function obtenerProductos(referencia, categoria) {
  productos.splice(0, productos.length);
  const message = [
    null,
    `No existe un producto con la referencia ${referencia}`,
  ];
  await $("#table_products > tbody").empty();
  let body;
  if (referencia) {
    body = await doFetch("get", "products/" + referencia, message, 200);
    if (body) {
      productos.push(body);
      llenarProductos();
    }
    return;
  }
  if (categoria) {
    body = await doFetch(
      "get",
      "products?categoryId=" + categoria,
      message,
      200
    );
    if (body) {
      for (let producto of body.products) {
        productos.push(producto);
      }
      llenarProductos();
    }
  }
  return body;
}

async function llenarIngresos(){
  await $("#table_entry > tbody").empty();
  const childs = new Array();
  const fragment = document.createDocumentFragment();
  const cantidadEdit = $("#editar-cantidad-ingreso")[0];
  for (let ingreso of ingresos) {
    const child = document.createElement("tr");
    const id = document.createElement("td");
    const referencia = document.createElement("td");
    const producto = document.createElement("td");
    const proveedor = document.createElement("td");
    const cantidadTotal = document.createElement("td");
    const costo = document.createElement("td");
    const fechaHora = document.createElement("td");

    id.textContent = ingreso.id;
    referencia.textContent = ingreso.referencia;
    producto.textContent = ingreso.producto;
    proveedor.textContent = ingreso.proveedor;
    cantidadTotal.textContent = ingreso.cantidadTotal
    costo.textContent = formatter.format(ingreso.costoxunidad);
    fechaHora.textContent = ingreso.fecha+" - "+ingreso.hora;

    child.appendChild(id);
    child.appendChild(referencia);
    child.appendChild(producto);
    child.appendChild(proveedor);
    child.appendChild(cantidadTotal);
    child.appendChild(costo);
    child.appendChild(fechaHora);
    childs.push(child);
    fragment.appendChild(child);
  }
  try {
    await Promise.all(
      childs.map(async (child) => {
        const acciones = await $.get("./accionesIngresos.html");
        const newChild = document.createElement("td");
        newChild.innerHTML = acciones;
        child.appendChild(newChild);
      })
    );
  } catch (err) {
    console.log(`Error: ${err.message}`);
  }

  const root = await $("#tableBody_ingresos");
  append(root, fragment);
  agregarEventListener(
    document.getElementsByClassName("btn-info-entry"),
    llenarInformacionIngreso
  );
  agregarEventListener(
    document.getElementsByClassName("btn-edit-entry"),
    llenarEdicionIngreso
  );
  agregarEventListener(
    document.getElementsByClassName("btn-delete-entry"),
    //almacenarReferenciaProducto
  );

  $("#mostrar-etiqueta-ingreso")[0].addEventListener('click',()=>{
    eventoLlenarCantidadEtiquetaIngreso($("#mostrar-etiqueta-ingreso option:selected").attr("id"),
    "mostrar-cantidad-ingreso");
  })
  $("#editar-etiqueta-ingreso")[0].addEventListener('click',()=>{
    eventoLlenarCantidadEtiquetaIngreso($("#editar-etiqueta-ingreso option:selected").attr("id"),
    "editar-cantidad-ingreso",true);
  })
    
cantidadEdit.addEventListener('keydown',(e)=>{
    if((e.keyCode < 48 || e.keyCode > 57)  && e.keyCode != 46 && e.keyCode != 8
    && e.keyCode != 37 && e.keyCode != 38 && e.keyCode != 39 && e.keyCode != 40){
      e.preventDefault();
    }
})
cantidadEdit.addEventListener('input', (e)=>{
  const valorString = e.target.value+'';
  let valor = 0;
  if(valorString.length > 0){
    valor = Number.parseInt(valorString);

  }
  const idEtiqueta = $("#editar-etiqueta-ingreso option:selected").attr('id');
  const etiquetaEditar = etiquetasEditadas.find(e => e.id==idEtiqueta);
  const inputCantidadTotal = $("#editar-cantidadTotal-ingreso")[0];
  const cantidadTotalValor = Number.parseInt(inputCantidadTotal.value);
  inputCantidadTotal.value = cantidadTotalValor-etiquetaEditar.cantidad+valor;
  etiquetaEditar.cantidad = Number.parseInt(valor);
  })
}

async function llenarProductos() {
  const childs = new Array();
  const fragment = document.createDocumentFragment();
  for (let producto of productos) {
    const child = document.createElement("tr");
    const referencia = document.createElement("td");
    const nombre = document.createElement("td");
    const categoria = document.createElement("td");
    const costo = document.createElement("td");

    referencia.textContent = producto.referencia;
    nombre.textContent = producto.nombre;
    costo.textContent = formatter.format(producto.costoxunidad);
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
    await Promise.all(
      childs.map(async (child) => {
        const acciones = await $.get("./accionesProductos.html");
        const newChild = document.createElement("td");
        newChild.innerHTML = acciones;
        child.appendChild(newChild);
      })
    );
  } catch (err) {
    console.log(`Error: ${err.message}`);
  }

  const root = await $("#tableBody_productos");
  append(root, fragment);
  agregarEventListener(
    document.getElementsByClassName("btn-info-product"),
    llenarInformacionProducto
  );
  agregarEventListener(
    document.getElementsByClassName("btn-edit-product"),
    llenarEdicionProducto
  );
  agregarEventListener(
    document.getElementsByClassName("btn-delete-product"),
    almacenarReferenciaProducto
  );
}

async function llenarTablaCategorias() {
  const childs = new Array();
  await $("#table_categories > tbody").empty();
  const fragment = document.createDocumentFragment();
  for (let categoria of categorias) {
    if (categoria.id != -1 && categoria.id != 0) {
      const child = document.createElement("tr");
      const id = document.createElement("td");
      const nombre = document.createElement("td");

      id.textContent = categoria.id;
      nombre.textContent = categoria.nombre;
      child.appendChild(id);
      child.appendChild(nombre);
      childs.push(child);
      fragment.appendChild(child);
    }
  }
  console.log("FRAGMENTO: ", categorias);
  try {
    await Promise.all(
      childs.map(async (child) => {
        const acciones = await $.get("./accionesCategorias.html");
        const newChild = document.createElement("td");
        newChild.innerHTML = acciones;
        child.appendChild(newChild);
      })
    );
  } catch (err) {
    console.log(`Error: ${err.message}`);
  }

  const root = await $("#tableBody_categorias");
  console.log("root", root);
  append(root, fragment);
  agregarEventListener(
    document.getElementsByClassName("btn-edit-category"),
    llenarEdicionCategoria
  );
  agregarEventListener(
    document.getElementsByClassName("btn-delete-category"),
    almacenarCategoria
  );
}

//mensajes = [407_Message,404_Message,400_Message]
async function doFetch(metodo, recurso, mensajes, estadoOk, json) {
  const settings = {
    method: metodo,
    headers: { Authorization: token, "Access-Control-Allow-Origin": "*" },
  };
  if (json) {
    settings.headers["Content-Type"] = "application/json";
    settings.body = JSON.stringify(json);
  }
  console.log(domain + recurso + " :", settings);

  let body = -1;
  try {
    const res = await fetch(domain + recurso, settings);

    if(res.status == 400){
      const json = await res.json();
      alert(json.errorMessage);
    }
    if (res.status == 403) {
      alert("Sesión terminada. Vuelva a iniciar sesión.");
      window.open("../login.html", "_self");
    }
    if (res.status == 404) {
      alert(mensajes[1]);
    }
    if (res.status == 409) {
      alert(mensajes[0]);
    }
    if (res.status >= 500) {
      console.log(res);
      alert("Error interno en el servidor. Contacte a soporte.");
    }
    if (res.status == estadoOk) {
      body = await res.json();
      return body;
    }
  } catch (err) {
    alert(`Ocurrió el siguiente error: ${err}`);
  }
  return body;
}

function llenarInformacionIngreso(ref){
  registroIngreso = ref;
  const referencia = $("#mostrar-referencia-ingreso")[0];
  const producto = $("#mostrar-producto-ingreso")[0];
  const etiqueta = $("#mostrar-etiqueta-ingreso")[0];
  $('#mostrar-etiqueta-ingreso').empty()
  const cantidad = $("#mostrar-cantidad-ingreso")[0];
  const cantidadTotal = $("#mostrar-cantidadTotal-ingreso")[0];

  const item = ingresos.find((i) => i.id == ref);
  const etiquetas = item.etiquetas;

  referencia.value = item.referencia;
  producto.value = item.producto;
  cantidad.value = etiquetas[0].cantidad;
  cantidadTotal.value = item.cantidadTotal;

  const fragment = document.createDocumentFragment();
  // etiqueta.addEventListener('click', (ev)=>{
  //   eventoLlenarCantidadEtiqueta($( "#mostrar-etiqueta-inventario option:selected")
  //   .attr('id'),"mostrar-referencia-inventario","mostrar-cantidad-inventario");
  // });
  for(eti of etiquetas){
    
    const option = document.createElement('option');
    option.innerText = eti.nombre;
    option.id = eti.id;
    
    fragment.appendChild(option);
  }
  etiqueta.appendChild(fragment);
}


function llenarInformacionInventario(ref){
  const referencia = $("#mostrar-referencia-inventario")[0];
  const producto = $("#mostrar-producto-inventario")[0];
  const etiqueta = $("#mostrar-etiqueta-inventario")[0];
  $('#mostrar-etiqueta-inventario').empty()
  const cantidad = $("#mostrar-cantidad-inventario")[0];
  const cantidadTotal = $("#mostrar-cantidadTotal-inventario")[0];

  const item = inventario.find((i) => i.referencia == ref);
  const etiquetas = item.etiquetas;

  referencia.value = item.referencia;
  producto.value = item.producto;
  cantidad.value = etiquetas[0].cantidad;
  cantidadTotal.value = item.cantidadTotal;

  const fragment = document.createDocumentFragment();
  // etiqueta.addEventListener('click', (ev)=>{
  //   eventoLlenarCantidadEtiqueta($( "#mostrar-etiqueta-inventario option:selected")
  //   .attr('id'),"mostrar-referencia-inventario","mostrar-cantidad-inventario");
  // });
  for(eti of etiquetas){
    
    const option = document.createElement('option');
    option.innerText = eti.nombre;
    option.id = eti.id;
    
    fragment.appendChild(option);
  }
  etiqueta.appendChild(fragment);
}

function llenarEdicionInventario(ref){
  etiquetasEditadas.splice(0,etiquetasEditadas.length);
  const referencia = $("#editar-referencia-inventario")[0];
  const producto = $("#editar-producto-inventario")[0];
  const etiqueta = $("#editar-etiqueta-inventario")[0];
  $('#editar-etiqueta-inventario').empty()
  const cantidad = $("#editar-cantidad-inventario")[0];
  const cantidadTotal = $("#editar-cantidadTotal-inventario")[0];

  const item = inventario.find((i) => i.referencia == ref);
  const etiquetas = item.etiquetas;

  referencia.value = item.referencia;
  producto.value = item.producto;
  cantidad.value = etiquetas[0].cantidad;
  cantidadTotal.value = item.cantidadTotal;

  const fragment = document.createDocumentFragment();
  // etiqueta.addEventListener('click', (ev)=>{
  //   eventoLlenarCantidadEtiqueta($( "#editar-etiqueta-inventario option:selected")
  //   .attr('id'),"editar-referencia-inventario","editar-cantidad-inventario");
  // });
  for(eti of etiquetas){
    etiquetasEditadas.push({id:eti.id,cantidad:eti.cantidad});
    const option = document.createElement('option');
    option.innerText = eti.nombre;
    option.id = eti.id;
    
    fragment.appendChild(option);
  }
  etiqueta.appendChild(fragment);
}

function llenarEdicionIngreso(ref){
  registroIngreso = ref;
  etiquetasEditadas.splice(0,etiquetasEditadas.length);
  const referencia = $("#editar-referencia-ingreso")[0];
  const producto = $("#editar-producto-ingreso")[0];
  const etiqueta = $("#editar-etiqueta-ingreso")[0];
  $('#editar-etiqueta-ingreso').empty()
  const cantidad = $("#editar-cantidad-ingreso")[0];
  const cantidadTotal = $("#editar-cantidadTotal-ingreso")[0];
  const proveedor = $("#editar-proveedor-ingreso")[0];
  const costo = $("#editar-costo-ingreso")[0];
  const fecha = $("#editar-fecha-ingreso")[0];
  const hora = $("#editar-hora-ingreso")[0];

  const item = ingresos.find((i) => i.id == ref);
  const etiquetas = item.etiquetas;

  referencia.value = item.referencia;
  producto.value = item.producto;
  cantidad.value = etiquetas[0].cantidad;
  cantidadTotal.value = item.cantidadTotal;
  proveedor.value = item.proveedor;
  costo.value = item.costoxunidad;
  fecha.value = parseDateToHTMLFormat(item.fecha);
  hora.value = item.hora;

  const fragment = document.createDocumentFragment();
  for(eti of etiquetas){
    etiquetasEditadas.push({id:eti.id,cantidad:eti.cantidad});
    const option = document.createElement('option');
    option.innerText = eti.nombre;
    option.id = eti.id;
    
    fragment.appendChild(option);
  }
  etiqueta.appendChild(fragment);

}

function parseDateToHTMLFormat(date){
  formated = "";
  for(char of date){
    if(char == '/'){
      formated+='-'
    }else{
      formated+=char;
    }

  }
  return formated;
}

function parseDateToAPIFormat(){
  const fecha = $("#editar-fecha-ingreso")[0].value;
  formated = "";
  for(char of fecha){
    if(char == '-'){
      formated+='/'
    }else{
      formated+=char;
    }

  }
  return formated;
}

function eventoLlenarCantidadEtiqueta(id,idReferencia,idCantidad, etiquetasTemporales){
  const referencia = $(`#${idReferencia}`)[0].value;
  const cantidadEtiqueta = $(`#${idCantidad}`)[0];
  let item;
  let etiquetas;
  let etiqueta;
  if(etiquetasTemporales){
    etiqueta = etiquetasEditadas.find(eti => eti.id == id);
  }else {
    item = inventario.find(item => item.referencia == referencia);
    etiquetas = item.etiquetas;
    etiqueta = etiquetas.find(eti => eti.id == id);
  }
  cantidadEtiqueta.value = etiqueta.cantidad;
}

function eventoLlenarCantidadEtiquetaIngreso(id,idCantidad, etiquetasTemporales){
  console.log("ID: "+id);
  const cantidadEtiqueta = $(`#${idCantidad}`)[0];
  let item;
  let etiquetas;
  let etiqueta;
  if(etiquetasTemporales){
    etiqueta = etiquetasEditadas.find(eti => eti.id == id);
  }else {
    item = ingresos.find(i => i.id == registroIngreso);
    etiquetas = item.etiquetas;
    etiqueta = etiquetas.find(eti => eti.id == id);
  }
  cantidadEtiqueta.value = etiqueta.cantidad;
}

function llenarInformacionProducto(ref) {
  const referencia_element = $("#mirar-referencia-producto")[0];
  const nombre_element = $("#mirar-nombre-producto")[0];
  const descripcion_element = $("#mirar-descripcion-producto")[0];
  const costo_element = $("#mirar-cu-producto")[0];
  const umbral_element = $("#mirar-umbral-producto")[0];
  const categoria_element = $("#mirar-categoria-producto")[0];

  const producto = productos.find((p) => p.referencia == ref);

  referencia_element.value = producto.referencia;
  nombre_element.value = producto.nombre;
  descripcion_element.innerText = producto.descripcion;
  costo_element.value = producto.costoxunidad;
  umbral_element.value = producto.umbral;
  categoria_element.value = producto.categoriaReference.nombre;
}

function llenarEdicionProducto(ref) {
  const referencia_element = $("#editar-referencia-producto")[0];
  const nombre_element = $("#editar-nombre-producto")[0];
  const descripcion_element = $("#editar-descripcion-producto")[0];
  const costo_element = $("#editar-cu-producto")[0];
  const umbral_element = $("#editar-umbral-producto")[0];
  const categoria_element = $("#editar-categoria-producto")[0];

  const producto = productos.find((p) => p.referencia == ref);

  referencia_element.value = producto.referencia;
  nombre_element.value = producto.nombre;
  descripcion_element.value = producto.descripcion;
  costo_element.value = producto.costoxunidad;
  umbral_element.value = producto.umbral;
  categoria_element.value = producto.categoriaReference.nombre;
}

function llenarEdicionCategoria(ref) {
  const nombre = $("#editar-nombre-categoria")[0];
  const categoria = categorias.find((c) => c.id == ref);
  nombre.value = categoria.nombre;
  nombre.name = categoria.id;
  console.log("CATEGORIA ID : ", nombre);
}

function llenarCategoriaModal(select_node) {
  const fragment = crearFragmentoCategorias(true);
  append(select_node, fragment);
}

function llenarEdicionEtiqueta(ref) {
  const nombre = $("#editar-nombre-etiqueta")[0];

  const etiqueta = etiquetas.find((e) => e.id == ref);

  nombre.value = etiqueta.nombre;
  registroEtiqueta = etiqueta.id;
}

function agregarEventListener(elementos, funcion) {
  const array = Array.from(elementos);
  console.log(`Elementos:`, elementos);
  array.forEach(function (e) {
    const parentArray =
      e.parentElement.parentElement.getElementsByTagName("td");
    const referencia = parentArray[0].innerText;
    e.addEventListener("click", () => {
      funcion(referencia);
    });
  });
}

async function editarInformacionProducto() {
  if (!validarCamposEditarProducto()) {
    alert("Debe editar un producto con campos válidos");
    return;
  }

  const referencia_element = $("#editar-referencia-producto")[0].value;
  const nombre_element = $("#editar-nombre-producto")[0].value;
  const descripcion_element = $("#editar-descripcion-producto")[0].value;
  const costo_element = $("#editar-cu-producto")[0].value;
  const umbral_element = $("#editar-umbral-producto")[0].value;
  const categoria_element = $(
    "#editar-categoria-producto option:selected"
  ).attr("id");

  let body = {
    nombre: nombre_element,
    descripcion: descripcion_element,
    costoxunidad: costo_element,
    umbral: umbral_element,
    categoria: categoria_element,
  };

  body = await doFetch(
    "put",
    "products/" + referencia_element,
    null,
    200,
    body
  );
  if (body != -1) {
    alert(`Se editó el producto ${referencia_element} satisfactoriamente.`);
    limpiarCamposEditarProducto();
    $("#modal_editarProducto").modal("hide");
  }
}

async function eliminarProducto() {
  const body = await doFetch(
    "delete",
    "products/" + registroReferencia,
    null,
    200
  );
  console.log(`Producto elimnado: ${body}`);
  if (body != -1) {
    alert(`Se eliminó el producto ${registroReferencia} satisfactoriamente.`);
  }

  $("#modal_eliminarProducto").modal("hide");
}

function almacenarReferenciaProducto(ref) {
  const confirmacion = $("#confirmacion-del-producto")[0];
  registroReferencia = ref;
  confirmacion.innerText = `¿Seguro que quiere elmininar el producto ${ref}?`;
}

function almacenarCategoria(ref) {
  const confirmacion = $("#confirmacion-del-categoria")[0];
  registroCategoria = ref;
  confirmacion.innerText = `¿Seguro que quiere elmininar la categoria ${registroCategoria}?`;
}

function almacenarEtiqueta(ref) {
  const confirmacion = $("#confirmacion-del-etiqueta")[0];
  registroEtiqueta = ref;
  confirmacion.innerText = `Al eliminar la etiqueta ${registroEtiqueta} su inventario asignado estará sin clasificar. ¿Está seguro?`;
}

async function actualizarInventario(){
  const ref = $("#editar-referencia-inventario")[0].value;
  body = {
    etiquetas: etiquetasEditadas
  }
  body = await doFetch(
    "put",
    "inventory",
    [null,"No se encontró una etiqueta."],
    200,
    body
  );
  if (body != -1) {
    alert(`El inventario del producto ${ref} se editó satisfactoriamente`);
    $("#modal_editarInventario").modal("hide");
    // limpiarCamposCrearCategoria();
    // actualizarRegistroCategorias();
  }
}

async function actualizarIngreso(){
  if(!validarCamposEditarIngreso()){
    alert("Debe ingresar campos válidos para editar el ingreso");
    return;
  }
  const proveedor = $("#editar-proveedor-ingreso")[0].value;
  const costoxunidad = $("#editar-costo-ingreso")[0].value;
  const fecha = parseDateToAPIFormat($("#editar-fecha-ingreso")[0].value);
  const hora = $("#editar-hora-ingreso")[0].value;
  body = {
    etiquetas: etiquetasEditadas,
    proveedor,
    costoxunidad,
    fecha,
    hora
  }
  console.log("FECHA: "+fecha+" HORA "+hora);
  body = await doFetch(
    "put",
    "inventory-entries/"+registroIngreso,
    [null,"No se encontró una etiqueta."],
    200,
    body
  );
  if (body != -1) {
    alert(`El ingreso ${registroIngreso} se editó satisfactoriamente`);
    $("#modal_editarIngreso").modal("hide");
  }
}

async function crearProducto() {
  if (!validarCamposCrearProducto()) {
    alert("Debe crear un producto con campos válidos");
    return;
  }
  const referencia_element = $("#crear-referencia-producto")[0].value;
  const nombre_element = $("#crear-nombre-producto")[0].value;
  const descripcion_element = $("#crear-descripcion-producto")[0].value;
  const costo_element = $("#crear-cu-producto")[0].value;
  const umbral_element = $("#crear-umbral-producto")[0].value;
  const categoria_element = $("#crear-categoria-producto option:selected").attr(
    "id"
  );

  let body = {
    referencia: referencia_element,
    nombre: nombre_element,
    descripcion: descripcion_element,
    costoxunidad: costo_element,
    umbral: umbral_element,
    categoria: categoria_element,
  };

  body = await doFetch(
    "post",
    "products",
    [`Ya existe un producto con la referencia ${referencia_element}`],
    201,
    body
  );
  if (body != -1) {
    alert(`El producto ${referencia_element} se creó satisfactoriamente`);
    $("#agregar").modal("hide");
    limpiarCamposCrearProducto();
  }
}

async function crearCategoria() {
  if (!validarCamposCrearCategoria()) {
    alert("Debe crear una categoría con un nombre válido");
    return;
  }

  let body = {
    nombre: $("#crear-nombre-categoria")[0].value.trim(),
  };

  body = await doFetch(
    "post",
    "categories",
    [`Ya existe una categoria con el nombre ${body.nombre}`],
    201,
    body
  );
  if (body != -1) {
    alert(`La categoria ${body.nombre} se creó satisfactoriamente`);
    $("#modal_agregarCategoria").modal("hide");
    limpiarCamposCrearCategoria();
    actualizarRegistroCategorias();
  }
}

async function crearEtiqueta() {
  if (!validarCamposCrearEtiqueta()) {
    alert("Debe crear una etiqueta con nombre y referencia válidos");
    return;
  }

  let body = {
    producto: $("#crear-referencia-etiqueta")[0].value.trim(),
    nombre: $("#crear-nombre-etiqueta")[0].value.trim(),
  };

  body = await doFetch(
    "post",
    "labels",
    [
      `Ya existe una categoria con el nombre ${body.nombre} para el producto con la referencia ${body.producto}`,
      `No existe un producto con la referencia ${body.producto}`,
    ],
    201,
    body
  );
  if (body != -1) {
    alert(`La etiqueta ${body.nombre} se creó satisfactoriamente`);
    $("#modal_agregarEtiqueta").modal("hide");
    limpiarCamposCrearEtiqueta();
  }
}

async function eliminarEtiqueta() {
  const body = await doFetch("delete", "labels/" + registroEtiqueta, null, 200);
  if (body != -1) {
    alert(
      `Se eliminó la etiqueta ${registroEtiqueta} satisfactoriamente. Su inventario asignado está sin clasificar.`
    );
  }
  $("#modal_eliminarEtiqueta").modal("hide");
}

async function editarCategoria() {
  if (!validarCamposEditarCategoria()) {
    alert("Debe editar una etiqueta con un nombre válido");
    return;
  }
  const nombre = $("#editar-nombre-categoria")[0];
  let body = {
    nombre: nombre.value.trim(),
  };

  body = await doFetch(
    "put",
    "categories/" + nombre.name,
    [`Ya existe una categoria con el nombre ${body.nombre}`],
    200,
    body
  );
  if (body != -1) {
    alert(`La categoria ${nombre.name} se editó satisfactoriamente`);
    $("#modal_editarCategoria").modal("hide");
    limpiarCamposCrearCategoria();
    actualizarRegistroCategorias();
  }
}

async function eliminarCategoria() {
  const body = await doFetch(
    "delete",
    "categories/" + registroCategoria,
    [`Existen productos que utilizan la categoría ${registroCategoria}`],
    200
  );
  if (body != -1) {
    alert(`Se eliminó la categoría ${registroCategoria} satisfactoriamente.`);
    actualizarRegistroCategorias();
  }
  $("#modal_eliminarCategoria").modal("hide");
}

async function editarEtiqueta() {
  if (!validarCamposEditarEtiqueta()) {
    alert("Debe editar una etiqueta con un nombre válido");
    return;
  }
  const nombre = $("#editar-nombre-etiqueta")[0];
  let body = {
    nombre: nombre.value.trim(),
  };

  body = await doFetch(
    "put",
    "labels/" + registroEtiqueta,
    [`Ya existe una etiqueta con el nombre ${body.nombre}`],
    200,
    body
  );
  if (body != -1) {
    alert(`La etiqueta ${nombre.name} se editó satisfactoriamente`);
    $("#modal_editarEtiqueta").modal("hide");
  }
}

function limpiarCamposCrearProducto() {
  const referencia_element = ($("#crear-referencia-producto")[0].value = "");
  const nombre_element = ($("#crear-nombre-producto")[0].value = "");
  const descripcion_element = ($("#crear-descripcion-producto")[0].value = "");
  const costo_element = ($("#crear-cu-producto")[0].value = "");
  const umbral_element = ($("#crear-umbral-producto")[0].value = "");
}

function limpiarCamposCrearCategoria() {
  const nombre = ($("#crear-nombre-categoria")[0].value = "");
}

function limpiarCamposEditarProducto() {
  const referencia_element = ($("#editar-referencia-producto")[0].value = "");
  const nombre_element = ($("#editar-nombre-producto")[0].value = "");
  const descripcion_element = ($("#editar-descripcion-producto")[0].value = "");
  const costo_element = ($("#editar-cu-producto")[0].value = "");
  const umbral_element = ($("#editar-umbral-producto")[0].value = "");
}

function limpiarCamposCrearEtiqueta() {
  const referencia_element = ($("#crear-referencia-etiqueta")[0].value = "");
  const nombre_element = ($("#crear-nombre-etiqueta")[0].value = "");
}

function validarCamposCrearCategoria() {
  const nombre = $("#crear-nombre-categoria")[0].value.trim();
  return nombre;
}

function validarCamposEditarCategoria() {
  const nombre = $("#editar-nombre-categoria")[0].value.trim();
  return nombre;
}

function validarCamposCrearProducto() {
  const referencia_element = $("#crear-referencia-producto")[0].value.trim();
  const nombre_element = $("#crear-nombre-producto")[0].value.trim();
  const descripcion_element = $("#crear-descripcion-producto")[0].value.trim();
  const costo_element = $("#crear-cu-producto")[0].value.trim();
  const umbral_element = $("#crear-umbral-producto")[0].value.trim();
  return (
    referencia_element &&
    nombre_element &&
    descripcion_element &&
    Number.parseInt(costo_element) > 0 &&
    Number.parseInt(umbral_element) > 0
  );
}

function validarCamposEditarProducto() {
  const nombre_element = $("#editar-nombre-producto")[0].value.trim();
  const descripcion_element = $("#editar-descripcion-producto")[0].value.trim();
  const costo_element = $("#editar-cu-producto")[0].value.trim();
  const umbral_element = $("#editar-umbral-producto")[0].value.trim();
  return (
    nombre_element &&
    descripcion_element &&
    Number.parseInt(costo_element) > 0 &&
    Number.parseInt(umbral_element) > 0
  );
}

function validarCamposCrearEtiqueta() {
  const referencia_element = $("#crear-referencia-etiqueta")[0].value.trim();
  const nombre_element = $("#crear-nombre-etiqueta")[0].value.trim();

  return referencia_element && nombre_element;
}

function validarCamposEditarEtiqueta() {
  const nombre = $("#editar-nombre-etiqueta")[0].value.trim();
  return nombre;
}

function validarCamposEditarIngreso() {
  const proveedor = $("#editar-proveedor-ingreso")[0].value.trim();
  let costo = $("#editar-costo-ingreso")[0].value.trim();
  const fecha = $("#editar-fecha-ingreso")[0].value.trim();
  const hora = $("#editar-hora-ingreso")[0].value.trim();

  try {
    costo = Number.parseInt(costo);
  }catch(e){
    return false;
  }
  return proveedor && fecha && hora && (costo > 0);
}

async function actualizarRegistroCategorias() {
  await obtenerCategorias(false);
  llenarTablaCategorias();
}
