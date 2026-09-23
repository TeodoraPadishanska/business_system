import { updateCartPrice, checkLoginStatus } from "./pages-js/common.js";

checkLoginStatus();

fetch("http://localhost:8080/business/products")
.then(response => response.json())
.then(products => {
    let container = document.getElementById('product-cards');
    container.innerHTML = ``;

    products.forEach(product => {
        const card = createProductCard(product);
        container.append(card);
        console.log(product);
    })

})
.catch(error => console.log(error));

updateCartPrice();

function calculateDiscount(product) {
    if (product.isOnSale == null){
        product.isOnSale = false;
    }
    if(product.isOnSale || product.price > 0){
        return Math.round(((product.price - product.salePrice) / product.price) * 100);
    }
    return 0;
}
function createProductCard(product) {
    const card = document.createElement('div');
    const discount = calculateDiscount(product)
    card.className = 'card';
    card.style.width = '18rem';

    card.innerHTML = `
        ${product.isOnSale ? `<span class = "discount">- ${discount}%</span>` : ""}
        <button class="favourite-btn">♡</button>
        <img class="card-img-top" src="${product.imgUrl}" alt="${product.name}">
        <div class="card-body">
            <h5 class="card-title">${product.name}</h5>
        </div>
        <div class="price-container">
            <div class="price-row">
                <span class="new-price">
                    ${product.isOnSale ? product.salePrice : product.price}&nbsp;€
                </span>
                ${product.isOnSale ? `<s class="old-price">${product.price}&nbsp;€</s>` : ""}
            </div>
        </div>

    <button onclick="addToCart(${product.id})" class="add_item btn btn-outline-dark" style="margin: 12px">Добави</button>`;
    return card;
}

async function addToCart(id){
    console.log(id);

    const res = await fetch(`http://localhost:8080/business/cart/${id}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        }

    });

    if(!res.ok){
        const responseJson = await res.json();
        alert(responseJson.message);
    }
    await updateCartPrice();



}


