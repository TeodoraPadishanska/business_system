const token = localStorage.getItem("token");


async function loadCart() {
    await fetch("http://localhost:8080/business/cart", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    })
        .then(response => {
            if (response.status === 403 || response.status === 401) {
                let forbiddenErrorMessage = document.createElement("div");
                forbiddenErrorMessage.textContent = `Не сте влязли в профила си или сесията ви е изтекла.`;
                document.getElementById("cart-items").appendChild(forbiddenErrorMessage);
                throw new Error("UNAUTHORIZED");
            }
            if (!response.ok) {
                throw new Error(response.statusText)
            }
            return response.json();
        })
        .then(data => {

            // TODO: fix inconsistent sorting
            data.sort((a, b) => {a.product.name.localeCompare(b.product.name);});
            console.log(data);
            console.log(data.items);
            let cartItemsDiv = document.getElementById('cart-items');
            cartItemsDiv.innerHTML = ``;
            if(data.length === 0) {
                let forbiddenErrorMessage = document.createElement("div");
                forbiddenErrorMessage.textContent = `Количката Ви е праззна.`;
                let homeBtn = document.createElement("button");
                homeBtn.className = "btn";
                homeBtn.id="home-btn";
                homeBtn.innerHTML = "Начало";
                homeBtn.addEventListener("click", () => {
                    window.location.href = "../index.html";
                });
                document.getElementById("cart-items").appendChild(forbiddenErrorMessage).appendChild(homeBtn);
            }else{
                cartItemsDiv.innerHTML = ``;
                data.forEach(product => {
                    cartItemsDiv.append(loadCartProduct(product));
                    console.log(product);
                })
            }
        }).catch(error => console.log(error));
}

loadCart();


function loadCartProduct(product) {
    let cartItem = document.createElement('div');
    cartItem.setAttribute('class', 'cart-item');
    cartItem.innerHTML = `
        <div class="cart-product" id='cart-product-${product.product.id}'>
            <div>
              <img class="cart-product-img me-4" src="${product.product.imgUrl}" alt="${product.name}">
              <a class="product-name">${product.product.name}</a>
            </div>
          
            <div class="product-quantity">
                <button class="btn product-quantity me-2"></button>
                    <input id="product-quantity-${product.product.id}" 
                           class="product-quantity-input form-control" 
                           type="number"  
                           min="1" 
                           value="${product.quantity}"
                           onChange=updateQuantity(${product.product.id})                    
                    />
                <button class="btn product-quantity"></button>
                <div>
                    <span id="product-price-${product.product.id}">${(product.product.price * product.quantity).toPrecision(2)}</span>
                </div>
                <div id="cart-remove-item-btn"><button type="button" class="btn-close" onclick="removeProduct(${product.product.id})" aria-label="Close"></button></div>
            </div>
            
            
        </div>`
    return cartItem;
}


async function updateQuantity(productId){
    const productInput = document.getElementById(`product-quantity-${productId}`);

    const response = await fetch(`http://localhost:8080/business/cart/${productId}/${productInput.value}`, {
       method: "PATCH",
       headers: {
           Authorization: `Bearer ${token}`,
       }
    });
    if (!response.ok) {
        throw new Error(response.statusText)
    }
    // await loadCart();
    const productPrice = document.getElementById(`product-price-${productId}`);

    const items = await response.json();
    console.log(items);

    const product = items.filter(ci => ci.product.id === productId)[0];
    console.log(product);

    productPrice.textContent = (product.product.price * product.quantity).toPrecision(2);

}

async function removeProduct(productId) {
    const product = document.getElementById(`cart-product-${productId}`);
    const response = await fetch(`http://localhost:8080/business/cart/${productId}`,{
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    });
    if (!response.status === 204) {
        alert(response.statusText);
    }else{
        //TODO kogato nqma poveche produkti da ti pokazva suobshtenie bez da se prezarejda stranicata
        product.remove();
    }
}