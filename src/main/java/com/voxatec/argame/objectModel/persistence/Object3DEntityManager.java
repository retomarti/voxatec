package com.voxatec.argame.objectModel.persistence;

import java.io.UnsupportedEncodingException;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;
import java.util.Base64;

import org.springframework.web.util.HtmlUtils;

import com.voxatec.argame.objectModel.beans.File;
import com.voxatec.argame.objectModel.beans.Object3D;


public class Object3DEntityManager extends EntityManager {
	
	
	// ---- Get all objects ------------------------------------------------------------------------------
	public Vector<Object3D> getObjects3D() throws SQLException {
		Vector<Object3D> object3DList = new Vector<Object3D>();

		try {
			this.initConnection();

			String stmt = "select * from object3D order by name";
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			Object3D obj3D = null;

			while (resultSet.next()) {
				// Object3D
				obj3D = new Object3D();
				obj3D.setId(resultSet.getInt("id"));
				obj3D.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
				obj3D.setText(HtmlUtils.htmlEscape(resultSet.getString("text")));
				obj3D.setObjFileName(HtmlUtils.htmlEscape(resultSet.getString("obj_file_name")));
				object3DList.add(obj3D);
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return object3DList;
	}
	
	
	// ---- Get one object by Id -------------------------------------------------------------------------	
	public Object3D getObject3DById(Integer object3DId) throws SQLException {
		Object3D theObject3D = null;
		
		try {
			this.initConnection();

			String template = "select * from object3D where id=%d";
			String stmt = String.format(template, object3DId);
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				theObject3D = new Object3D();
				theObject3D.setId(object3DId);
				theObject3D.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
				theObject3D.setText(HtmlUtils.htmlEscape(resultSet.getString("text")));
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return theObject3D;
	}
	

	// ---- Create an object ----------------------------------------------------------------------------
	public Object3D createObject3D(Object3D object3D) throws SQLException {
		
		if (object3D == null)
			return null;
		
		try {
			this.initConnection();

			// Insert adventure into DB
			String template = "insert into object3D (name,text) values (\"%s\",\"%s\")";
			String name = HtmlUtils.htmlUnescape(object3D.getName());
			String text = HtmlUtils.htmlUnescape(object3D.getText());
			String stmt = String.format(template, name, text);
			this.connection.executeUpdateStatement(stmt);
			
			// Retrieve auto inserted ID value
			Integer lastInsertedId = this.getLastAutoInsertedId();
			object3D.setId(lastInsertedId);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
		
		return object3D;
	}
	
	
	// ---- Update an object ---------------------------------------------------------------------------
	public void updateObject3D(Object3D object3D) throws SQLException {
		
		if (object3D == null)
			return;
		
		try {
			this.initConnection();

			String template = "update object3D set name=\"%s\", text=\"%s\" where id=%d";
			String name = HtmlUtils.htmlUnescape(object3D.getName());
			String text = HtmlUtils.htmlUnescape(object3D.getText());
			Integer id  = object3D.getId();
			String stmt = String.format(template, name, text, id);
			
			this.connection.executeUpdateStatement(stmt);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
	
	
	// ---- Delete object -------------------------------------------------------------------------
	public void deleteObject3D(Integer object3DId) throws SQLException {
		
		Object3D theObject3D = null;
		
		if (object3DId == -1)
			return;   // nothing to do
		
		try {
			theObject3D = this.getObject3DById(object3DId);
			
			this.initConnection();
			
			if (theObject3D != null) {
				// Delete object3D
				String template = "delete from object3D where id=%d";
				String stmt = String.format(template, object3DId);
				this.connection.executeUpdateStatement(stmt);
			}
		
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}

	
	
	// ---- Get image of object -------------------------------------------------------------------------
	public byte[] getImage(Integer object3DId) throws SQLException {
		byte[] imgData = "".getBytes();
		
		try {
			this.initConnection();
			
	        String template = "select image from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("image");
				if (blob != null) {
					imgData = blob.getBytes(1, (int) blob.length());
				}
			}
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return imgData;
	}
	
	
	// ---- Update image of object -------------------------------------------------------------------------
	public void updateImage(File imageFile, Integer object3DId) throws SQLException, UnsupportedEncodingException {
		
		try {
			System.out.println(String.format("content size: %d", imageFile.getContent().length()));

			this.initConnection();
			PreparedStatement stmt = this.connection.newPreparedStatement("update object3D set image=? where id=?");
			
			// Decode image from Base64
			byte[] image = null;
			if (imageFile != null && imageFile.getContent() != null) {
				byte[] bytes = imageFile.getContent().getBytes();
				image = Base64.getDecoder().decode(bytes);
			}
			// System.out.println(String.format("decoded size: %d", decoded.length));
			
			// Bind statement parameters & execute
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, image);
			stmt.setBlob(1, blob);
	        stmt.setInt(2, object3DId);
	        stmt.executeUpdate();
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
	
	}
	
	
	// ---- Get DAT file of object group ---------------------------------------------------------------------
	public File getDatFile(Integer cacheGroupId) throws SQLException {
		File datFile = new File();
		
        try {
			this.initConnection();

	        String template = "select target_img_dat_file_name, target_img_dat_file from cache_group where id=%d";
	        String stmt = String.format(template, cacheGroupId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("target_img_dat_file");
				if (blob != null) {
					String strContent = new String(blob.getBytes(1l, (int) blob.length()));
					datFile.setContent(HtmlUtils.htmlEscape(strContent));
					datFile.setMimeType("application/octet-stream");
				}
				datFile.setName(resultSet.getString("target_img_dat_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return datFile;
	}
	

	// ---- Get OBJ file of object -------------------------------------------------------------------------
	public String getObjFile(Integer object3DId) throws SQLException {
		String objFile = null;
		
        try {
			this.initConnection();

	        String template = "select obj_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("obj_file");
				if (blob != null) {
					objFile = new String(blob.getBytes(1l, (int) blob.length()));
					objFile = HtmlUtils.htmlEscape(objFile);
				}
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return objFile;
	}
	
	
	// ---- Update OBJ file of object ----------------------------------------------------------------------
	public void updateObjFile(File objFile, Integer object3DId) throws SQLException {
		
		try {
			this.initConnection();
			
			PreparedStatement stmt = this.connection.newPreparedStatement("update object3D set obj_file=?, obj_file_name=? where id=?");
			
			// Unescape file content
			String fileContent = HtmlUtils.htmlUnescape(objFile.getContent());
			
			// Bind statement parameters & execute
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, fileContent.getBytes());
			stmt.setBlob(1, blob);
	        stmt.setString(2, objFile.getName());
	        stmt.setInt(3, object3DId);
	        stmt.executeUpdate();
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
	}
	
	
	// ---- Get MTL file of object -------------------------------------------------------------------------
	public String getMtlFile(Integer object3DId) throws SQLException {
		String mtlFile = null;
		
        try {
			this.initConnection();

	        String template = "select mtl_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("mtl_file");
				if (blob != null) {
					mtlFile = new String(blob.getBytes(1l, (int) blob.length()));
					mtlFile = HtmlUtils.htmlEscape(mtlFile);
				}
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
		
		return mtlFile;
	}
	
	
	// ---- Update MTL file of object ----------------------------------------------------------------------
	public void updateMtlFile(File mtlFile, Integer object3DId) throws SQLException {
		
		try {
			this.initConnection();
			
			PreparedStatement stmt = this.connection.newPreparedStatement("update object3D set mtl_file=?, mtl_file_name=? where id=?");
			
			// Unescape file content
			String fileContent = HtmlUtils.htmlUnescape(mtlFile.getContent());
			
			// Bind statement parameters & execute
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, fileContent.getBytes());
			stmt.setBlob(1, blob);
	        stmt.setString(2, mtlFile.getName());
	        stmt.setInt(3, object3DId);
	        stmt.executeUpdate();
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
	}
	
	
	// ---- Get TEX file of object -------------------------------------------------------------------------
	public byte[] getTexFile(Integer object3DId, String texFileName) throws SQLException {
		byte[] imgData = "".getBytes();
		
		try {
			this.initConnection();
			
			PreparedStatement stmt = this.connection.newPreparedStatement("select image from texture where object3D_id=? and name like ?");

	        stmt.setInt(1, object3DId);
	        stmt.setString(2, texFileName + "%");
	        ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("image");
				if (blob != null) {
					imgData = blob.getBytes(1, (int) blob.length());
				}
			}
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return imgData;
	}
		
	
	// ---- Update TEX file of object ----------------------------------------------------------------------
	public void updateTexFile(File texFile, Integer object3DId) throws SQLException {
		
		try {
			this.initConnection();
			
			PreparedStatement stmt = this.connection.newPreparedStatement("update object3D set tex_file=?, tex_file_name=? where id=?");
			
			// Decode tex image file from Base64
			byte[] image = null;
			if (texFile != null && texFile.getContent() != null) {
				byte[] bytes = texFile.getContent().getBytes();
				image = Base64.getDecoder().decode(bytes);
			}
			// System.out.println(String.format("decoded size: %d", decoded.length));
			
			// Bind statement parameters & execute
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, image);
			stmt.setBlob(1, blob);
	        stmt.setString(2, texFile.getName());
	        stmt.setInt(3, object3DId);
	        stmt.executeUpdate();
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
	}

		
}
