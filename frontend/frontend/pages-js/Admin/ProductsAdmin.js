import productEdit from "./ProductEdit.js";

export default async function productsAdmin() {


    const response = await fetch("http://localhost:8080/business/products");

    if(!response.ok){
        throw new Error(`Request failed: ${response.status}`);
    }



    const products = await response.json();
    products.sort((a, b) => a.id - b.id);

    return `
    <section class="orders" id="orders">
        <h2>Products</h2>

        <table id="products-table">
            <thead>
            <tr class="table-columns">
                <th>Product ID</th>
                <th>Img</th>
                <th>Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>On sale</th>
            </tr>
            </thead>

            <tbody>
            ${products.map(product => `
                <tr>
                    <td># ${product.id}</td>
                    <td><img height="100vw" src="${product.imgUrl}" alt="${product.name}"></td>
                    <td>${product.name}</td>
                    <td>€ ${product.price.toFixed(2)}</td>
                    <td><div class="product-quantity-at-stock">${product.quantityAtStock}</div></td>
                    <td>
                        <img
                            src="${product.isOnSale
                                ? '../../images/tick.jpeg'
                                : '../../images/cross.webp'}"
                            alt="${product.isOnSale ? 'On sale' : 'Not on sale'}"
                            width="24"
                            height="24"
                        >
                    </td>
                    <td>
                    <button class="util-btn edit-btn" id="btn-edit-${product.id}">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-fill" viewBox="0 0 16 16">
                          <path d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.5.5 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11z"/>
                        </svg>
                    </button>
                    <button id="btn-delete-${product.id}" class="util-btn trash-btn">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash3-fill" viewBox="0 0 16 16">
                          <path d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5m-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5M4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06m6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528M8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5"/>
                        </svg>
                    </button>
                    </td>
                </tr>
            `).join(``)}
            </tbody>
        </table>
    </section>
    `;
}