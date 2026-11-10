import dashboard from "./Admin/Dashboard.js";
import productsAdmin from "./Admin/ProductsAdmin.js";
import ordersAdmin from "./Admin/OrdersAdmin.js";
import usersAdmin from "./Admin/UsersAdmin.js";
import productEdit from "./Admin/ProductEdit.js";
import orderEdit from "./Admin/OrderEdit.js";


const mainBody = document.getElementById("dashboard");

function clearActive(){
    const links = document.querySelectorAll(".sidebar a");
    links.forEach(link => {link.classList.remove("active");});
}

// NAVIGATION

const linkProducts=  document.getElementById("link-products");
linkProducts.addEventListener("click", async ()=>{
    await loadProducts();
});

const linkDashboard=  document.getElementById("link-dashboard");
linkDashboard.addEventListener("click", async ()=>{
    await loadDashboard();
});

const linkOrders = document.getElementById("link-orders");
linkOrders.addEventListener("click", async ()=>{
    await loadOrders();
})

const linkUsers =  document.getElementById("link-users");
linkUsers.addEventListener("click", async ()=>{
    await loadUsers();
})

// LOAD PAGE

async function loadLastPage(){
    switch (localStorage.getItem("page")) {
        case "dashboard":
            await loadDashboard();
            break;
        case "products":
            await loadProducts();
            break;
        case "orders":
            await loadOrders();
            break;
        case "users":
            await loadUsers();
            break;
        default:
            await loadDashboard();
            break;

    }
}

async function loadDashboard(){
    mainBody.innerHTML=await dashboard();
    clearActive();
    linkDashboard.classList.toggle("active");
    localStorage.setItem("page","dashboard");
}

async function loadProducts(){
    mainBody.innerHTML=await productsAdmin();
    clearActive();
    linkProducts.classList.toggle("active");
    localStorage.setItem("page","products");

    document.querySelectorAll(".edit-btn").forEach(btn => {
        btn.addEventListener("click", async ()=>{
            const productId = btn.id.substring(btn.id.lastIndexOf("-")+1);
            console.log(productId);
            await loadProductEdit(productId);
        })
    });

    document.querySelectorAll(".trash-btn").forEach(btn => {
        btn.addEventListener("click", async ()=>{
            const productId = btn.id.substring(btn.id.lastIndexOf("-")+1);
            console.log(productId);

            if(confirm("Delete?")){
                const response = await fetch(`http://localhost:8080/business/products/${productId}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });

                if(response.ok){
                    await loadProducts();
                }
            }
        })
    });



}

async function loadOrders(){
    mainBody.innerHTML=await ordersAdmin();
    clearActive();
    linkOrders.classList.toggle("active");
    localStorage.setItem("page","orders");

    document.querySelectorAll(".edit-btn").forEach(btn => {
        btn.addEventListener("click", async ()=>{
            const orderId = btn.id.substring(btn.id.lastIndexOf("-")+1);
            console.log(orderId);
            await loadOrderEdit(orderId);
        })
    });


    document.querySelectorAll(".trash-btn").forEach(btn => {
        btn.addEventListener("click", async ()=>{
            const orderId = btn.id.substring(btn.id.lastIndexOf("-")+1);
            console.log(orderId);

            if(confirm("Delete?")){
                const response = await fetch(`http://localhost:8080/business/orders/${orderId}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });

                if(response.ok){
                    await loadOrders();
                }
            }
        })
    });
}

async function loadUsers(){
    mainBody.innerHTML=await usersAdmin();
    clearActive();
    linkUsers.classList.toggle("active");
    localStorage.setItem("page","users");
}

async function loadOrderEdit(orderId) {

    console.log(orderId);

    const orderEditBody = await orderEdit(orderId);

    if (orderEditBody) {

        mainBody.innerHTML = orderEditBody;

        document.getElementById("order-edit-cancel-btn").addEventListener("click", async () => {
            await loadOrders();
        });

        const form = document.getElementById("edit-order-form");

        form.addEventListener("submit", async (e) => {

            e.preventDefault();

            if (!confirm("Save changes?")) {
                return;
            }

            const formData = new FormData(form);

            const order = {
                email: formData.get("email"),
                firstName: formData.get("firstName"),
                lastName: formData.get("lastName"),
                company: formData.get("company"),
                phoneNumber: formData.get("phoneNumber"),

                address: formData.get("address"),
                city: formData.get("city"),
                country: formData.get("country"),
                postalCode: formData.get("postalCode"),

                expectedDelivery: formData.get("expectedDelivery") || null,

                orderStatus: formData.get("orderStatus"),
                paymentStatus: formData.get("paymentStatus")
            };

            const response = await fetch(`http://localhost:8080/business/orders/${orderId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify(order)
            });

            if (response.ok) {
                await loadOrders();
            } else {
                const error = await response.text();
                console.error("Failed to update order:", error);
                alert("Error updating order!");
            }

        });

    } else {
        alert("Error");
    }
}

async function loadProductEdit(productId){
    console.log(productId);
    const productEditBody = await productEdit(productId);
    if(productEditBody){
        mainBody.innerHTML=productEditBody;


        document.getElementById("product-edit-cancel-btn").addEventListener("click", async ()=>{
            await loadProducts();
        });

        const form = document.getElementById("edit-product-form");


        form.addEventListener("submit", async (e) => {
            e.preventDefault();

            if(!confirm("Save changes?")){
                return;
            }

            const formData = new FormData(form);

            const product = {
                name: formData.get("name"),
                brand: formData.get("brand"),
                barcode: formData.get("barcode"),
                categoryId: Number(formData.get("category")),
                price: Number(formData.get("price")),
                weightQuantity: Number(formData.get("weightQuantity")) || null,
                unit: formData.get("unit"),
                quantityAtStock: Number(formData.get("quantityAtStock")),
                salePrice: Number(formData.get("salePrice")) || null,
                imgUrl: formData.get("imgUrl"),
                isOnSale: formData.has("isOnSale"),
                isAvailable: formData.has("isAvailable")
            };

            const response = await fetch(`http://localhost:8080/business/products/${productId}`,{
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify(product),
            });

            if (response.ok) {
                await loadProducts();
            }
        });
    }
    else{
        alert("Error")
    }

}




loadLastPage();










