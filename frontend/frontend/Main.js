let products = [];
fetch("http://localhost:8080/business/products")
.then(response => response.json())
.then(data => {
    products = data;
    let container = document.getElementById('products');
    data.forEach((product) => {
        container.innerHTML += `<div class="product"> <h2>${product.name}</h2><p>ID: ${product.id}</p></div>`
    });
})
.catch(error => console.log(error));