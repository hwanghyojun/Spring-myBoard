package com.example.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.BoardDao;
import com.example.service.BoardService;
import com.example.domain.BoardVO;
import com.example.paging.PageCriteria;

// @Service: 해당 서비스 객체가 자동으로 생성
// @Repository, @Component등과 유사하다
@Service("BoardService")
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDao boardDao;
	@Override
	public List<BoardVO> listPageCriteria(PageCriteria pc) throws Exception {
		return boardDao.listPageCriteria(pc);
	}
	@Override
	public Integer totalCount() throws Exception {
		return boardDao.totalCount();
	}
	@Override
	public BoardVO boardRead(HashMap<String, Object> params) throws Exception {
		return boardDao.boardRead(params);
	}
	@Override
	public void countHit(int b_num) throws Exception {
		boardDao.countHit(b_num);
	}
	@Override
	public void boardWrite(HashMap<String, Object> params) throws Exception {
		boardDao.boardWrite(params);
	}
	@Override
	public void boardUpdate(HashMap<String, Object> params) throws Exception {
		boardDao.boardUpdate(params);
	}
	@Override
	public void boardDelete(HashMap<String, Object> params) throws Exception {
		boardDao.boardDelete(params);
	}
	@Override
	public void countReply(int b_num) throws Exception {
		boardDao.countReply(b_num);
	}
	@Override
	public List<BoardVO> searchBoard(HashMap<String, Object> search_params) throws Exception {
		return boardDao.searchBoard(search_params);
	}
	@Override
	public Integer searchTotalCount(HashMap<String, Object> params) throws Exception {
		return boardDao.searchTotalCount(params);
	}
	@Override
	public void countRecommend(HashMap<String, Object> params) throws Exception {
		boardDao.countRecommend(params);
	}
}
