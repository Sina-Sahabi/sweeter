package com.sinarmin.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinarmin.server.dataAccess.BlockDAO;
import com.sinarmin.server.models.Block;
import java.sql.SQLException;
import java.util.List;

public class BlockController {
	private final BlockDAO blockDAO;
	public BlockController() throws SQLException {
		blockDAO = new BlockDAO();
	}

	public void createBlockTable() throws SQLException {
		blockDAO.createBlockTable();
	}

	public void saveBlock(String blocker, String blocked) throws SQLException {
		Block block = new Block(blocker, blocked);
		blockDAO.saveBlock(block);
	}

	public void deleteBlock(String blocker, String blocked) throws SQLException {
		Block block = new Block(blocker, blocked);
		blockDAO.deleteBlock(block);
	}

	public void deleteAll() throws SQLException {
		blockDAO.deleteAll();
	}

	public String getBlocks(String userId) throws SQLException, JsonProcessingException {
		List<Block> blocks = blockDAO.getBlocks(userId);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(blocks);
	}

	public String getBlockers(String userId) throws SQLException, JsonProcessingException {
		List<Block> blocks = blockDAO.getBlockers(userId);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(blocks);
	}

	public String getAll() throws SQLException, JsonProcessingException {
		List<Block> blocks = blockDAO.getAll();
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(blocks);
	}

	public boolean isBlocking(String blockerId, String blockedId) throws SQLException {
		return blockDAO.isBlocking(blockerId, blockedId);
	}
}