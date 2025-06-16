const accessToken = localStorage.getItem('accessToken');
const itemId = localStorage.getItem('editItemId');

// 페이지 로드 시 기존 item 데이터 불러오기
window.addEventListener('DOMContentLoaded', async function () {
    const response = await fetch(`/api/item/${itemId}`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + accessToken
        }
    });

    if (response.ok) {
        const item = await response.json();
        document.getElementById('name').value = item.name;
        document.getElementById('description').value = item.description;
        document.getElementById('price').value = item.price;
        document.getElementById('type').value = item.type;
        document.getElementById('isStackable').checked = item.stackable;
    } else {
        alert('Failed to load item data');
    }
});

// 수정 form submit
document.getElementById('update-item-form').addEventListener('submit', async function (e) {
    e.preventDefault();

    const item = {
        name: document.getElementById('name').value,
        description: document.getElementById('description').value,
        price: parseInt(document.getElementById('price').value),
        type: document.getElementById('type').value,
        isStackable: document.getElementById('isStackable').checked
    };

    const response = await fetch(`/admin/item/update/${itemId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + accessToken
        },
        body: JSON.stringify(item)
    });

    if (response.ok) {
        alert('Item updated!');
        window.location.href = '/admin/index.html';
    } else {
        alert('Failed to update item');
    }
});
