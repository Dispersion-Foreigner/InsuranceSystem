package Control;

import Contract.Contract;
import Contract.ContractList;
import Contract.ContractListImpl;
import Customer.Customer;
import Customer.CustomerListImpl;
import Insurance.InsuranceList;
import Customer.CutomerList;

import java.util.ArrayList;
import java.util.Date;

public class Sales {
	private InsuranceList insuranceList;
	private CutomerList cutomerList;
	private ContractList contractList;
	private CalculatePremium calculatePremium;

	public Sales(InsuranceList insuranceList, CutomerList cutomerList, ContractList contractList, CalculatePremium calculatePremium) {
		this.insuranceList = insuranceList;
		this.cutomerList = cutomerList;
		this.contractList = contractList;
	}

	//총 고객 수 구하기
	public int totalCustomerCount() {
		return this.cutomerList.getSize();
	}

	//이번 달 가입한 고객 수 구하기
	public int thisMonthCustomerCount() {
		int count = 0;
		Date date = new Date();
		if (this.cutomerList instanceof CustomerListImpl) {
			for (Customer customer : ((CustomerListImpl) this.cutomerList).getCustomerList()) {
				if (customer.getJoinDate().getMonth() == date.getMonth()) {
					count++;
				}
			}
		}
		return count;
	}
	//미납한 고객 수 구하기
	public int unpaidCustomerCount() {
		int count = 0;
		if (this.cutomerList instanceof CustomerListImpl) {
			for (Customer customer : ((CustomerListImpl) this.cutomerList).getCustomerList()) {
				if (!customer.isPay()) {
					count++;
				}
			}
		}
		return count;
	}

	//고객List 전달
	public ArrayList<Customer> getTotalCustomer() {
		return ((CustomerListImpl) this.cutomerList).getCustomerList();
	}

	//고객 정보 수정하기
	public void updateCustomer(int customerId, String address, String phoneNum, String email, String job, String medical) {
		Customer customer = this.cutomerList.get(customerId);
		customer.setAddress(address);
		customer.setPhoneNumber(phoneNum);
		customer.setEmail(email);
//		customer.getMedicalHistory().setMyDisease();

		//update 구현해야 할 수 있음
	}

	//고객 정보 삭제하기
	public void deleteCustomer(int customerId) {
		this.cutomerList.delete(customerId);
	}

	//미납한 고객 구하기
	public ArrayList<Customer> getUnpaidCustomer() {
		ArrayList<Customer> unpaidCustomerList =new ArrayList<>();
		if (this.cutomerList instanceof CustomerListImpl) {
			for (Customer customer : ((CustomerListImpl) this.cutomerList).getCustomerList()) {
				if (!customer.isPay()) {
					unpaidCustomerList.add(customer);
				}
			}
		}
		return unpaidCustomerList;
	}

	//미납한 고객 돈 받기
	public void payCustomer(ArrayList<Customer> unpaidCustomerList) {
		for (Customer customer : unpaidCustomerList) {
			customer.setPay(true);
		}
	}

	//보험마다 계약한 고객 수 구하기
	public int countContractCustomer(int insuranceId) {
		int count = 0;
		if (this.contractList instanceof ContractListImpl) {
			for (Contract contract : ((ContractListImpl) this.contractList).getContractList()) {
				if (contract.getInsuranceId() == insuranceId) {
					count++;
				}
			}
		}
		return count;
	}


	//가입자 id, 보험 id 입력했을 경우로 유스케이스 수정
	public Contract findContract(int customerId, int insuranceId) {
		if (this.contractList instanceof ContractListImpl) {
			for (Contract contract : ((ContractListImpl) this.contractList).getContractList()) {
				if (contract.getInsuranceId() == insuranceId && contract.getCustomerId() == customerId) {
					return contract;
				}
			}
		}
		return null;
	}

	//보험 계약 해지
	public void closeContract(int contractId) {
		this.contractList.delete(contractId);
	}

	//보험 계약 체결
	//Main에서 Customer를 새로 만들고 전달하는 식으로
	//고객을 먼저 추가하고 가입을 할지 가입 할 때 저장을 할지 정확하게 정해야 함
	//일단 가입 시 고객을 저장하는 버전
	public void signContract(int insuranceId, Customer customer) {
		this.cutomerList.add(customer);
		Contract contract = new Contract();
		contract.setContractId(this.contractList.getSize() + 1);
		contract.setCustomerId(customer.getId());
		contract.setInsuranceId(insuranceId);
		contract.setPremiumRate(this.insuranceList.get(insuranceId).getPremium());
		contract.setInsurancePrice(
				(int) this.calculatePremium.calculatePremium(customer, this.insuranceList.get(insuranceId).getPremium()));

		this.contractList.add(contract);
	}














}
