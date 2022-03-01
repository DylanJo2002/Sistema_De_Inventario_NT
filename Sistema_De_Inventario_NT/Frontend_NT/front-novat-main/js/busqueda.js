// $(function() {
//     console.log("BÚSQUEDA DE CATEGORÍAS HECHA");
//     obtenerCategorias();
// });   

// const domain = "http://localhost:8080/";
// let categorias;
// const token = 'Bearer '+localStorage.getItem('tokenNT');

// function obtenerCategorias(){
// 	fetch(domain+'categories',{
//         method: 'get',
//         headers: {'Authorization':token,
//                 'Access-Control-Allow-Origin': '*'}   
//     }).then((res)=>{
//         if(res.status != 200){
//             alert('Sesión terminada. Vuelva a iniciar sesión');
// 			window.open('../login.html',"_self");
//             return -1;   
//         }
//         return res.json();
//     }).then((body) => {
//         if(body != -1){
//             categorias = body.categories;
//             llenarCategorias();
// 			console.log(categorias);
//             return;
//         }
//         alert('Ocurrió un error al consultar las categorías.');
//     });
// }

// function llenarCategorias(){
//     const fragment = document.createDocumentFragment();
//     for(let categoria of categorias){
//         const child = document.createElement('option');
//         child.text = categoria.nombre;
//         child.tagName = categoria.id;
//         console.log(child);
//         fragment.appendChild(child);
//     }
//     const selectProductos = document.getElementById('select_categoryProduct');
//     const selectCategorias = document.getElementById('select_categoryCategory');

//     append(selectCategorias||selectProductos, fragment);

// }

// function append(root, fragment){
//     root.append(fragment);
// }


