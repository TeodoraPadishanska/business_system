import {
    updateCartPrice,
    checkLoginStatusNavBtn,
    checkBackendHealth,
    checkAdminStatusNavBtn
} from "./pages-js/common.js";

// checkBackendHealth();

checkLoginStatusNavBtn();
checkAdminStatusNavBtn();


async function loadProducts() {
    const container = document.getElementById("product-cards");
    container.innerHTML = "";

    try {
        const response = await fetch("http://localhost:8080/business/products");

        if (!response.ok) {
            throw new Error(`Request failed: ${response.status}`);
        }

        const products = await response.json();

        for (const product of products) {
            const card = await createProductCard(product);
            container.append(card);

            const favoriteBtn =
                document.getElementById(`favorite-btn-${product.id}`);

            // The button may not exist when the user is logged out.
            if (favoriteBtn) {
                favoriteBtn.addEventListener("click", async () => {
                    const result = await fetch(
                        `http://localhost:8080/business/users/favourite/toggle/${product.id}`,
                        {
                            method: "POST",
                            headers: {
                                Authorization: `Bearer ${localStorage.getItem("token")}`,
                            },
                        }
                    );

                    if (result.ok) {
                        favoriteBtn.classList.toggle("favourite-btn-active");
                    }
                });
            }

            const cartBtn =
                document.getElementById(`cart-add-btn-${product.id}`);

            cartBtn.addEventListener("click", () => {
                addToCart(product.id);
            });
        }
    } catch (error) {
        console.error("Could not load products:", error);
    }
}


loadProducts();

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

async function createProductCard(product) {
    const card = document.createElement('div');
    // const discount = calculateDiscount(product)
    card.className = 'card';
    card.style.width = '18rem';

    let isFavourite;
    const token = localStorage.getItem("token");
    if (token){
        const favourites= await getFavourites();
        const favoriteIds = favourites.map(item => item.id);

        console.log(favourites);

        isFavourite=favoriteIds.includes(product.id);
    }

    card.innerHTML = `
        ${product.isOnSale ? `<span class = "discount">- ${discount}%</span>` : ""}
        ${token ? `<button id="favorite-btn-${product.id}" class="favourite-btn ${isFavourite ? "favourite-btn-active" : ""}">♥</button>`:""}
        <img class="card-img-top" src="${product.imgUrl}" alt="${product.name}">
        <div class="card-body">
            <h5 class="card-title">${product.name}</h5>
        </div>
        <div class="price-container">
            <div class="price-row">
                <span class="new-price">
                    ${(product.isOnSale ? product.salePrice : product.price).toFixed(2)}&nbsp;€
                </span>
                ${product.isOnSale ? `<s class="old-price">${product.price}&nbsp;€</s>` : ""}
            </div>
        </div>

    <button id="cart-add-btn-${product.id}" class="add_item btn btn-outline-dark" style="margin: 12px">Добави</button>`;

    return card;
}

async function addToCart(productId){
    console.log(productId);

    const res = await fetch(`http://localhost:8080/business/cart/${productId}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        }

    });

    if(res.status === 403){
        alert("Влезте в профила си.");
        return;
    }
    showAddToCartMessage()
    await updateCartPrice();
}

function showAddToCartMessage(){

    const toastElement = document.getElementById("cart-toast");

    const toast = bootstrap.Toast.getOrCreateInstance(toastElement, {
        delay: 3000
    })
    toast.show();
}


async function getFavourites() {
    const response = await fetch("http://localhost:8080/business/users/favourite", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
    });

    if (response.ok) {
        return response.json();
    }
    return {};
}


