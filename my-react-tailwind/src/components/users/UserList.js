import React from 'react';

const UserList = ({ users }) => {

  if (!users || users.length === 0) {
    return <p className="text-center text-gray-400 mt-6">Aucun utilisateur trouvé.</p>;
  }

  return (
    <div className="bg-gray-800 shadow-lg rounded-lg overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-700">
        <thead className="bg-gray-700">
          <tr>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">ID</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Nom</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Email</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Rôle</th>
            {/* Ajoutez d'autres colonnes si nécessaire */}
            <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-300 uppercase tracking-wider">Actions</th>
          </tr>
        </thead>
        <tbody className="bg-gray-800 divide-y divide-gray-700">
          {users.map((user) => (
            <tr key={user.id} className="hover:bg-gray-700 transition duration-150">
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-100">{user.id}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">{user.name}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">{user.email}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm">
                 {user.email === 'admin@admin.com' ? 
                    <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-900 text-yellow-300">Admin</span> 
                    : <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-indigo-900 text-indigo-300">Utilisateur</span>
                 }
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-right text-sm text-gray-400 italic">
                 (Aucune action)
                 {/* Si l'API permettait de modifier/supprimer, les boutons seraient ici */}
                 {/* <button className="text-yellow-400 hover:text-yellow-300 mr-2">Modifier</button>
                 <button className="text-red-500 hover:text-red-400">Supprimer</button> */}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default UserList; 