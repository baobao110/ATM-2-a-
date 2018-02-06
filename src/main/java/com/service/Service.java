package com.service;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.AccountFlow.Account;
import com.BankCard.Card;
import com.base.DataBase;
import com.exception.newException;
import com.fenye.Fenye;
import com.flow.flow;
import com.inter.AccountDAO;
import com.inter.CardDAO;
import com.util.Dateformate;



@Component	//������Spring��ע������ʱSpring���Զ�ɨ���ע���������,���������Control��ʹ�õ�
@Transactional	//�����ע��������spring��mybaties���ʱ���������,����ͬ��mybaties�е�sqlSession��connection,���ﲻ��Ҫ�ֶ��Ŀ����͹ر�����
public class Service {
	
	@Autowired
	private CardDAO cardDao;
	
	@Autowired
	private AccountDAO accountDao;
	
	@Transactional(rollbackFor=Exception.class)
	public Card open(String username,String password) {
		Card card=new Card();
		card.setNumber(DataBase.CreateNumber());
		card.setPassword(DigestUtils.md5Hex(password));
		card.setMoney(0);
		card.setUsername(username);
		System.out.println(card);
		if(cardDao.open(card)==1) {//�������Card.xml���е�SQL���
			return card;
			}
		else {
			throw new newException("����ʧ��");
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void save(int number,String password,double money,String username)  {
			Card card=cardDao.GetCad(number);//������Ժ�֮ǰ��ʹ��mybaties�Ļ�ȡ������бȽϾͿ��Է�����spring+mybaties���ŵ�,�����Զ���������
			if(null==card) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			if(!DigestUtils.md5Hex(password).equals(card.getPassword())) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			if(money<0) {
				System.out.println("���С����");
				throw new newException("���С����");
			}
			double x=card.getMoney();
			x+=money;
			if(cardDao.modifyMoney(number, x)!=0) {
				System.out.println("��Ǯ�ɹ�");	
			}
			Account account=new Account();
			account.setNumber(number);
			account.setMoney(money);
			account.setType(1);
			account.setDescription("��Ǯ");
			account.setUsername(username);
			accountDao.add(account);
			System.out.println("��ˮ�˲���");
		}
		
	@Transactional(rollbackFor=Exception.class)
	public void draw(int number,String password,double money,String username) {
			Card card=cardDao.GetCad(number);
			if(null==card) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			if(!DigestUtils.md5Hex(password).equals(card.getPassword())) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			if(money<0) {
				System.out.println("���С����");
				throw new newException("���С����");
			}
			double x=card.getMoney();
			if(money>x) {
				System.out.println("����");
				throw new newException("����");
			}
			x-=money;
			if(cardDao.modifyMoney(number, x)!=0) {
				System.out.println("ȡǮ�ɹ�");	
			}
			Account account=new Account();
			account.setNumber(number);
			account.setMoney(money);
			account.setType(1);
			account.setDescription("ȡǮ");
			account.setUsername(username);
			accountDao.add(account);
			System.out.println("��ˮ�˲���");
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void transfer(int OutNumber,String password,double money,int InNumber,String username) {
			Card card1=cardDao.GetCad(OutNumber);
			if(null==card1) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
				
			}
			if(!DigestUtils.md5Hex(password).equals(card1.getPassword())) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			if(money<0) {
				System.out.println("���С����");
				throw new newException("���С����");
			}
			double x=card1.getMoney();
			if(money>x) {
				System.out.println("����");
				throw new newException("����");
			}
			x-=money;
			if(cardDao.modifyMoney(OutNumber, x)!=0) {
				System.out.println("ȡǮ�ɹ�");	
			}
			Account account1=new Account();
			account1.setNumber(OutNumber);
			account1.setMoney(money);
			account1.setType(1);
			account1.setDescription("ȡǮ");
			account1.setUsername(username);
			accountDao.add(account1);
			System.out.println("��ˮ�˲���");
			Card card2=cardDao.GetCad(InNumber);
			if(null==card2) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
				
			}
			double y=card2.getMoney();
			y+=money;
			if(cardDao.modifyMoney(InNumber, y)!=0) {
				System.out.println("ת��ɹ�");	
			}
			Account account2=new Account();
			account2.setNumber(InNumber);
			account2.setMoney(money);
			account2.setType(1);
			account2.setDescription("��Ǯ");
			account2.setUsername(username);
			accountDao.add(account2);
			System.out.println("��ˮ�˲���");
		}//����ע��cardDao��ʹ��
	
	@Transactional(rollbackFor=Exception.class)
	public Card ChangePassword(int number,String oldPassword,String newPassword) {
			Card a=cardDao.GetCad(number);
			if(null==a) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			if(!DigestUtils.md5Hex(oldPassword).equals(a.getPassword())) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			if(cardDao.modifyPassword(number, DigestUtils.md5Hex(newPassword))!=0) {
				a=cardDao.GetCad(number);
				return a;
			}
			else {
				 throw new newException("�˺Ż������벻����");
			}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Fenye List(int number, String password, int currentPage) {
		Card card=cardDao.GetCad(number);
		if(null==card) {
			System.out.println("�˺Ż������벻����");
			throw new newException("�˺Ż������벻����");
		}
		if(!DigestUtils.md5Hex(password).equals(card.getPassword())) {
			System.out.println("�˺Ż������벻����");
			throw new newException("�˺Ż������벻����");
		}
		int totalNumber=accountDao.totalNumber(number);//����totalNumber������ȡ�ܼ�¼��Ŀ
		Fenye fenye=new Fenye(totalNumber, currentPage);//��ʼ��������Ϊ�ܼ�¼�͵�ǰҳ��
		 ArrayList<Account>list=accountDao.List(number, fenye.getcurrentNumber(), fenye.move);//����List()������ȡ��number�ĸ�ҳ����
		 ArrayList<flow> account=new ArrayList<>(list.size());
			for(Account i:list) {
				flow a=new flow();
				a.setId(i.getId());
				a.setNumber(i.getNumber());
				a.setMoney(i.getMoney());
				a.setType(i.getType());
				a.setCreatetime(Dateformate.formate(i.getCreatetime()));
				a.setDescription(i.getDescription());
				account.add(a);
			}
		 fenye.setObject(account);//����ȡ�ļ�¼����
		 if(null==fenye) {
			 throw new newException("����Ϊ��");
		 }
		 else {
			 return fenye;
		 }
	}
	
	@Transactional(rollbackFor=Exception.class)
	public int total(int number) {
		return accountDao.totalNumber(number);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Card GetCard(int number) {
			Card a=cardDao.GetCad(number);
			if(null==a) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			else {
				return a;
			}
		}
	
	@Transactional(rollbackFor=Exception.class)
	public Card Get(int number,String password) {
			Card a=cardDao.GetCard(number,DigestUtils.md5Hex(password));
			if(null==a) {
				System.out.println("�˺Ż������벻����");
				throw new newException("�˺Ż������벻����");
			}
			else {
				return a;
			}
		}
	
	@Transactional(rollbackFor=Exception.class)//���ô���������쳣
	public void delete(int number) {
			int i=cardDao.delete(number);
			System.out.println(i);
			if(i==0) {
				System.out.println("�˺Ż������벻����");
			}
	}
	
	public ArrayList<flow> ten(String username){
		System.out.println("server username"+username);
		ArrayList<Account> account=accountDao.ten(username);
		ArrayList<flow> list=new ArrayList<>(account.size());
		for(Account i:account) {
			flow a=new flow();
			a.setId(i.getId());
			a.setNumber(i.getNumber());
			a.setMoney(i.getMoney());
			a.setType(i.getType());
			a.setCreatetime(Dateformate.formate(i.getCreatetime()));
			a.setDescription(i.getDescription());
			list.add(a);
		}
		return list;
	}
}
/*
 * ʲô�Ǽ�����쳣,ʲô�ǷǼ�����쳣,��������ַ�����:
 * 1 ������쳣�̳���Exception �Ǽ�����쳣�̳���RuntimException����Error
 * 2 ������쳣���벶��,���ǷǼ�����쳣���Բ�����
 * /
 */