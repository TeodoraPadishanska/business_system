export default async function productEdit(productId){


    const response = await fetch(`http://localhost:8080/business/products/${productId}`,{
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`
        },
    });


    const categoriesResponse = await fetch(`http://localhost:8080/business/categories`,{
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`
        },
    });

    if(!response.ok || !categoriesResponse.ok){
        return null;
    }

    const product = await response.json();
    const categories = await categoriesResponse.json();

    return `
        <section class="orders">
            <h2>Product Information</h2>
        
            <form id="edit-product-form">
        
                <input type="hidden" id="product-id" value="${product.id}">
        
                <div class="form-group">
                    <label for="imgUrl">Img</label>
                    <input type="text" id="imgUrl" name="imgUrl"
                           value="${product.imgUrl}" required>
                </div>
                
                <div class="form-group">
                    <label for="name">Product name</label>
                    <input type="text" id="name" name="name"
                           value="${product.name}" required>
                </div>
        
                <div class="form-group">
                    <label for="brand">Brand</label>
                    <input type="text" id="brand" name="brand"
                           value="${product.brand ?? ""}">
                </div>
        
                <div class="form-group">
                    <label for="barcode">Barcode</label>
                    <input type="text" id="barcode" name="barcode"
                           value="${product.barcode}" required>
                </div>
        
                <div class="form-group">
                    <label for="category">Category ID</label>
                    <select id="category" name="category" id="category">
                        ${
                            categories.map(c => `
                                <option value="${c.id}">${c.name}</option>
                            `).join("")
                        }
                    </select>
                </div>
        
                <div class="form-group">
                    <label for="price">Price (€)</label>
                    <input type="number" id="price" name="price"
                           min="0" step="0.01"
                           value="${product.price}" required>
                </div>
        
                <div class="form-group">
                    <label for="weightQuantity">Weight / Quantity</label>
                    <input type="number" id="weightQuantity"
                           name="weightQuantity" min="0" step="0.01"
                           value="${product.weightQuantity ?? ""}">
                </div>
        
                <div class="form-group">
                    <label for="unit">Unit</label>
                    <input type="text" id="unit" name="unit"
                           placeholder="kg, g, l, ml, pcs"
                           value="${product.unit ?? ""}">
                </div>
        
                <div class="form-group">
                    <label for="quantityAtStock">Quantity in stock</label>
                    <input type="number" id="quantityAtStock"
                           name="quantityAtStock" min="0"
                           value="${product.quantityAtStock}" required>
                </div>
        
                <div class="form-group">
                    <label for="salePrice">Sale price (€)</label>
                    <input type="number" id="salePrice" name="salePrice"
                           min="0" step="0.01"
                           value="${product.salePrice ?? ""}">
                </div>
        
                <div class="form-group">
                    <label for="imgUrl">Image URL</label>
                    <input type="url" id="imgUrl" name="imgUrl"
                           value="${product.imgUrl ?? ""}">
                </div>
        
                <div class="form-group checkbox-group">
                    <label for="isOnSale">On sale</label>
                    <input type="checkbox" id="isOnSale" name="isOnSale"
                           ${product.isOnSale ? "checked" : ""}>
                </div>
        
                <div class="form-group checkbox-group">
                    <label for="isAvailable">Available</label>
                    <input type="checkbox" id="isAvailable" name="isAvailable"
                           ${product.isAvailable ? "checked" : ""}>
                </div>
        
                <div class="form-actions">
                    <button type="submit" class="save-btn">
                        Save Changes
                    </button>
        
                    <button type="button" id="product-edit-cancel-btn" class="cancel-btn">
                        Cancel
                    </button>
                </div>
        
            </form>
        </section>

            
 
    `
}