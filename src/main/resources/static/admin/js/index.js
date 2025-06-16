const accessToken = localStorage.getItem('accessToken');

// 페이지 로드 시 토큰 없으면 로그인으로 리다이렉트 (보안)
if (!accessToken) {
    alert('로그인이 필요합니다.');
    window.location.href = '/admin/login.html';
}

// Load Items 버튼 이벤트
document.getElementById('load-items-btn').addEventListener('click', async function () {
    const response = await fetch('/api/item', {
        method: 'GET',
        headers: { 'Authorization': 'Bearer ' + accessToken }
    });

    if (response.ok) {
        const items = await response.json();
        const list = document.getElementById('item-list');
        list.innerHTML = '';

        items.forEach(item => {
            const li = document.createElement('li');
            li.innerHTML = `${item.id}: ${item.name} - ${item.price} 
                <button onclick="editItem(${item.id})">Edit</button> 
                <button onclick="deleteItem(${item.id})">Delete</button>`;
            list.appendChild(li);
        });
    } else {
        alert('Failed to load items');
    }
});

// Edit 버튼 클릭 시
function editItem(id) {
    localStorage.setItem('editItemId', id);
    window.location.href = '/admin/update-item.html';
}

// Delete 버튼 클릭 시
async function deleteItem(id) {
    const confirmed = confirm('정말 삭제하시겠습니까?');
    if (!confirmed) return;

    const response = await fetch(`/admin/item/delete/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + accessToken
        }
    });

    if (response.ok) {
        alert('삭제 성공!');
        document.getElementById('load-items-btn').click();  // Reload list
    } else {
        alert('삭제 실패!');
    }
}

// Logout 버튼 클릭 시
document.getElementById('logout-btn').addEventListener('click', function () {
    // (서버 logout API 호출 원하면 여기에 fetch 추가 가능)

    // 클라이언트 토큰 삭제
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');

    alert('로그아웃 되었습니다.');
    window.location.href = '/admin/login.html';
});
