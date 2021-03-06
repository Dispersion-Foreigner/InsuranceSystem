package Main;

import Controller.ContractService.CalculatePremium;
import Controller.ContractService.CalculatePremiumImpl;
import Controller.ContractService.ContractService;
import Controller.ContractService.Indemnification.AccidentSubjectIndemnification;
import Controller.ContractService.Indemnification.CarAccidentCauseIndemnification;
import Controller.ContractService.Indemnification.FireAccidentCauseIndemnification;
import Controller.ContractService.Indemnification.SeaAccidentCauseIndemnification;
import Controller.CustomerService.CustomerService;
import Controller.InsuranceService.InsuranceService;
import Controller.StaffService.StaffService;
import DAO.ContractDAO.DBContractDAO;
import DAO.CustomerDAO.*;
import DAO.InsuranceDAO.*;
import DAO.StaffDAO.StaffDAO;
import DAO.StaffDAO.DBStaffDAO;
import Domain.Contract.Contract;
import Domain.Customer.Customer;
import Domain.Insurance.Insurance;
import Domain.Staff.Staff;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Staff staff = null;

        StaffDAO staffDAO = new DBStaffDAO();

        FireInsuranceDAO fireInsuranceDAO = new DBFireInsuranceDAO();
        CarInsuranceDAO carInsuranceDAO = new DBCarInsuranceDAO();
        SeaInsuranceDAO seaInsuranceDAO = new DBSeaInsuranceDAO();

        DBMedicalHistoryDAO medicalHistoryDAO = new DBMedicalHistoryDAO();
        DBCarDAO carDAO = new DBCarDAO();
        DBHouseDAO houseDAO = new DBHouseDAO();
        DBShipDAO shipDAO = new DBShipDAO();

        DBInsuranceDAO insuranceDAO = new DBInsuranceDAO(carInsuranceDAO, fireInsuranceDAO, seaInsuranceDAO);
        DBCustomerDAO customerDAO = new DBCustomerDAO(medicalHistoryDAO, carDAO, houseDAO, shipDAO);
        DBContractDAO contractDAO = new DBContractDAO();

        CalculatePremium calculatePremium = new CalculatePremiumImpl();

        Date date = new Date();

        ContractService contractService = new ContractService(contractDAO, insuranceDAO, customerDAO, calculatePremium);
        CustomerService customerService = new CustomerService(customerDAO);
        InsuranceService insuranceService = new InsuranceService(insuranceDAO);
        StaffService staffService = new StaffService(staffDAO);

        main:
        while (true) {

            login:
            while (true) {
                System.out.println("========???????????? ????????? ????????????======== \n");
                System.out.println("1. ?????????");
                System.out.println("2. ????????????");

                String select1 = sc.nextLine();

                if (select1.equals("1")) {
                    // ?????? ?????????
                    System.out.println("id : ");
                    String inputId;

                    while (true) {
                        inputId = sc.nextLine();
                        if (!inputId.matches("[+-]?\\d*(\\.\\d+)?")) {
                            System.out.println("????????? ????????? ?????????.");
                            continue;
                        } else {
                            break;
                        }
                    }
                    System.out.println("pw : ");
                    String inputPW = sc.nextLine();

                    staff = staffService.login(Integer.parseInt(inputId), inputPW);

                    if (staff != null) {
                        break login;
                    } else {
                        System.out.println("????????? ????????? ????????????. ?????? ????????????????????? ?????? ?????? ??? ??????????????????");
                        continue login;
                    }
                } else if (Integer.parseInt(select1) == 2) {
                    // ?????? ??????
                    join:
                    while (true) {
                        System.out.println("?????? ?????? ??????");

                        System.out.println(
                                "?????? ?????? 1. ?????? / 2. ?????? ?????? / 3. ?????? ?????? / 4. ?????? ?????? / 5. ?????? ??????");
                        String department;

                        while (true) {
                            department = sc.nextLine();
                            if (!department.matches("[+-]?\\d*(\\.\\d+)?")) {
                                System.out.println("????????? ????????? ?????????.");
                                continue;
                            } else {
                                break;
                            }
                        }

                        System.out.println("?????? 1. ????????? / 2. ?????? / 3. ?????? / 4. ?????? / 5. ?????? / 6. ??????");
                        String position;

                        while (true) {
                            position = sc.nextLine();
                            if (!position.matches("[+-]?\\d*(\\.\\d+)?")) {
                                System.out.println("????????? ????????? ?????????.");
                                continue;
                            } else {
                                break;
                            }
                        }
                        System.out.println("????????? : ");
                        String id;

                        while (true) {
                            id = sc.nextLine();
                            if (!id.matches("[+-]?\\d*(\\.\\d+)?")) {
                                System.out.println("????????? ????????? ?????????.");
                                continue;
                            } else {
                                break;
                            }
                        }
                        System.out.println("???????????? : ");
                        String pw = sc.nextLine();
                        System.out.println("?????? : ");
                        String name = sc.nextLine();
                        System.out.println("SSN : ");
                        String ssn = sc.nextLine();
                        System.out.println("?????? : 1. ?????? / 2. ??????");
                        String gender;
                        while (true) {
                            gender = sc.nextLine();
                            if (!gender.matches("[+-]?\\d*(\\.\\d+)?")) {
                                System.out.println("????????? ????????? ?????????.");
                                continue;
                            } else {
                                break;
                            }
                        }
                        System.out.println("Email : ");
                        String email = sc.nextLine();
                        System.out.println("Phone : ");
                        String phone = sc.nextLine();

                        staff = staffService.createStaff(Integer.parseInt(id), pw, name, ssn,
                                Integer.parseInt(gender), email, phone, Integer.parseInt(department),
                                Integer.parseInt(position));

                        if (staff != null) {
                            System.out.println("????????? ??????????????? " + staff.getName() + "???!");
                            System.out.println(
                                    "ID??? " + staff.getId() + "?????? PW???" + staff.getPassword()
                                            + "?????????.");
                            break login;
                        } else {
                            System.out.println("????????? ?????????????????????. ?????? ??????????????????.");
                            continue join;
                        }
                    }

                }
            }
            selectWork:
            while (true) {
                System.out.println(staff.getId()
                        + " " + staff.getDepartment()
                        + " " + staff.getName()
                        + "??? ???????????????!");

                String select;
                switch (staff.getDepartment()) {
                    case Design:
                        System.out.println("1. ?????? ??????");
                        System.out.println("q. ????????????");
                        select = sc.nextLine();

                        switch (select) {
                            case "1":
                                design:
                                while (true) {

                                    System.out.println(
                                            "????????? ??? ?????? ?????? : " + insuranceService.computeTotalSize());
                                    System.out.println(
                                            "????????? ???????????? ????????? ?????? : " + insuranceService.computeAuthorizeCount());
                                    System.out.println("????????? ???????????? ?????? ????????? ?????? : "
                                            + insuranceService.computeNotAuthorizeCount());

                                    System.out.println("1. ?????? ?????? ????????????");
                                    System.out.println("2. ????????????");

                                    String select2 = sc.nextLine();

                                    if (select2.equals("1")) {
                                        selectDesign:
                                        while (true) {
                                            ArrayList<Insurance> insurances = insuranceService.getInsuranceList();
                                            if (insurances.isEmpty()) {
                                                System.out.println("????????? ???????????? ????????????. ?????? ????????? ??????????????????.");
                                            } else {
                                                for (Insurance insurance : insurances) {

                                                    System.out.println(
                                                            insurance.getId() + ". " + " ?????? ?????? : "
                                                                    + insurance.getType().name() + " ?????? ?????? : "
                                                                    + insurance.getName() + " ?????? ?????? : "
                                                                    + insurance.getExplanation() + " ?????? ??????: "
                                                                    + insurance.isAuthorization());
                                                }
                                            }


                                            System.out.println("1. ?????? ?????????");
                                            System.out.println("2. ?????? ??????");
                                            System.out.println("3. ?????? ??????");
                                            System.out.println("4. ????????????");

                                            String select3 = sc.nextLine();

                                            if (select3.equals("1")) {
                                                createInsurance:
                                                while (true) {
                                                    System.out.println(
                                                            "?????? ?????? : 1. ??????(??????) 2. ????????? 3. ??????");
                                                    String type;

                                                    while (true) {
                                                        type = sc.nextLine();
                                                        if (!type.matches("[+-]?\\d*(\\.\\d+)?")) {
                                                            System.out.println("????????? ????????? ?????????.");
                                                            continue;
                                                        } else {
                                                            break;
                                                        }
                                                    }
                                                    System.out.println("?????? ?????? : ");
                                                    String name = sc.nextLine();
                                                    System.out.println("?????? ?????? : ");
                                                    String explanation = sc.nextLine();
                                                    System.out.println("????????? : ");
                                                    String premium;

                                                    while (true) {
                                                        premium = sc.nextLine();
                                                        if (!premium.matches(
                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                            System.out.println("????????? ????????? ?????????.");
                                                            continue;
                                                        } else {
                                                            break;
                                                        }
                                                    }

                                                    String surroundingDamageBasicMoney = "0";
                                                    String humanDamageBasicMoney = "0";
                                                    String buildingDamageBasicMoney = "0";
                                                    String carDamageBasicMoney = "0";
                                                    String generalDamageBasicMoney = "0";
                                                    String revenueDamageBasicMoney = "0";

                                                    switch (type) {
                                                        case "1":
                                                            System.out.println("?????? ?????? ?????? ????????? : ");
                                                            while (true) {
                                                                surroundingDamageBasicMoney = sc.nextLine();
                                                                if (!surroundingDamageBasicMoney.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }

                                                            System.out.println("?????? ?????? ?????? ????????? : ");
                                                            while (true) {
                                                                humanDamageBasicMoney = sc.nextLine();
                                                                if (!humanDamageBasicMoney.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }

                                                            System.out.println("?????? ?????? ?????? ????????? : ");

                                                            while (true) {
                                                                buildingDamageBasicMoney = sc.nextLine();
                                                                if (!buildingDamageBasicMoney.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }

                                                            System.out.println("?????? ?????? ????????? ?????????????\n"
                                                                    + " ?????? ?????? : " + type + "\n"
                                                                    + " ?????? ?????? : " + name + "\n"
                                                                    + " ?????? ?????? : " + explanation + "\n"
                                                                    + " ????????? : " + premium + "\n"
                                                                    + " ?????? ?????? ?????? ????????? : : "
                                                                    + surroundingDamageBasicMoney + "\n"
                                                                    + " ?????? ?????? ?????? ????????? : : "
                                                                    + humanDamageBasicMoney + "\n"
                                                                    + " ?????? ?????? ?????? ????????? : : "
                                                                    + buildingDamageBasicMoney + "\n"
                                                                    + " 1. ??? 2. ????????? ");
                                                            break;
                                                        case "2":
                                                            System.out.println("????????? ?????? ?????? ????????? : ");

                                                            while (true) {
                                                                carDamageBasicMoney = sc.nextLine();
                                                                if (!carDamageBasicMoney.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }
                                                            System.out.println("?????? ?????? ?????? ????????? : ");

                                                            while (true) {
                                                                humanDamageBasicMoney = sc.nextLine();
                                                                if (!humanDamageBasicMoney.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }

                                                            System.out.println("?????? ?????? ????????? ?????????????\n"
                                                                    + " ?????? ?????? : " + type + "\n"
                                                                    + " ?????? ?????? : " + name + "\n"
                                                                    + " ?????? ?????? : " + explanation + "\n"
                                                                    + " ????????? : " + premium + "\n"
                                                                    + " ????????? ?????? ?????? ????????? : : "
                                                                    + carDamageBasicMoney + "\n"
                                                                    + " ?????? ?????? ?????? ????????? : : "
                                                                    + humanDamageBasicMoney + "\n"
                                                                    + " 1. ??? 2. ????????? ");
                                                            break;
                                                        case "3":
                                                            System.out.println("?????? ?????? ?????? ????????? : ");

                                                            while (true) {
                                                                generalDamageBasicMoney = sc.nextLine();
                                                                if (!generalDamageBasicMoney.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }
                                                            System.out.println("?????? ?????? ?????? ????????? : ");

                                                            while (true) {
                                                                revenueDamageBasicMoney = sc.nextLine();
                                                                if (!revenueDamageBasicMoney.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }

                                                            System.out.println("?????? ?????? ????????? ?????????????\n"
                                                                    + " ?????? ?????? : " + type + "\n"
                                                                    + " ?????? ?????? : " + name + "\n"
                                                                    + " ?????? ?????? : " + explanation + "\n"
                                                                    + " ????????? : " + premium + "\n"
                                                                    + " ?????? ?????? ?????? ????????? : : "
                                                                    + generalDamageBasicMoney + "\n"
                                                                    + " ?????? ?????? ?????? ????????? : : "
                                                                    + revenueDamageBasicMoney + "\n"
                                                                    + " 1. ??? 2. ????????? ");
                                                            break;
                                                    }

                                                    String answer = sc.nextLine();
                                                    if (answer.equals("1")) {
                                                        Insurance createInsurance = insuranceService.design(
                                                                Integer.parseInt(type), name,
                                                                explanation, Integer.parseInt(premium),
                                                                Integer.parseInt(
                                                                        surroundingDamageBasicMoney),
                                                                Integer.parseInt(humanDamageBasicMoney),
                                                                Integer.parseInt(
                                                                        buildingDamageBasicMoney),
                                                                Integer.parseInt(carDamageBasicMoney),
                                                                Integer.parseInt(
                                                                        generalDamageBasicMoney),
                                                                Integer.parseInt(
                                                                        revenueDamageBasicMoney));
                                                        if (createInsurance != null) {
                                                            staffService.addResult(staff);
                                                            System.out.println(
                                                                    "?????? ????????? ?????????????????????. ?????? ?????? ???????????? ????????? ????????? ?????? ????????? ????????? ??? ????????????.");
                                                            continue selectDesign;
                                                        } else {
                                                            System.out.println(
                                                                    "?????? ????????? ?????????????????????. ?????? ????????? ?????????.");
                                                            continue createInsurance;
                                                        }
                                                    } else if (answer.equals("2")) {
                                                        continue createInsurance;
                                                    }
                                                }
                                            } else if (select3.equals("2")) {
                                                authorize:
                                                while (true) {
                                                    System.out.println("=======?????? ?????? ??????=======");
                                                    for (Insurance insurance : insurances) {
                                                        if (!insurance.isAuthorization()) {
                                                            System.out.println(
                                                                    insurance.getId() + ". "
                                                                            + " ?????? ?????? : "
                                                                            + insurance.getType().name()
                                                                            + " ?????? ?????? : "
                                                                            + insurance.getName()
                                                                            + " ?????? ?????? : "
                                                                            + insurance.getExplanation()
                                                                            + " ?????? ??????: "
                                                                            + insurance.isAuthorization());
                                                        }
                                                    }
                                                    System.out.println();
                                                    System.out.println("1.?????? ??????");
                                                    System.out.println("2.?????? ??????");
                                                    String select4 = sc.nextLine();

                                                    if (select4.equals("1")) {
                                                        System.out.println("?????? ?????? ?????? id??? ????????? ?????????.");
                                                        String select5;

                                                        while (true) {
                                                            select5 = sc.nextLine();
                                                            if (!select5.matches(
                                                                    "[+-]?\\d*(\\.\\d+)?")) {
                                                                System.out.println("????????? ????????? ?????????.");
                                                                continue;
                                                            } else {
                                                                break;
                                                            }
                                                        }

                                                        System.out.println("?????? ????????? ?????? ???????????????????");
                                                        System.out.println("1. ???");
                                                        System.out.println("2. ?????????");

                                                        String select6 = sc.nextLine();


                                                        if (select6.equals("1")) {
                                                            if (insuranceService.authorize(Integer.parseInt(select5))) {
                                                                staffService.addResult(staff);
                                                                System.out.println(
                                                                        "?????? ????????? ????????? ?????? ???????????????.");
                                                                continue selectDesign;
                                                            } else {
                                                                System.out.println(
                                                                        "????????? id??? ????????? ????????????. ?????? ????????? ?????????.");
                                                                continue authorize;
                                                            }
                                                        } else if (select6.equals("2")) {
                                                            System.out.println("????????? ?????????????????????.");
                                                            continue authorize;
                                                        }


                                                    } else if (select4.equals("2")) {
                                                        continue selectDesign;
                                                    } else {
                                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                        continue authorize;
                                                    }
                                                }
                                            } else if (select3.equals("3")) {
                                                deleteInsurance:
                                                while (true) {
                                                    System.out.println("?????? ??? ????????? ID??? ????????? ?????????.");

                                                    String insuranceId;

                                                    while (true) {
                                                        insuranceId = sc.nextLine();
                                                        if (!insuranceId.matches("[+-]?\\d*(\\.\\d+)?")) {
                                                            System.out.println("????????? ????????? ?????????.");
                                                            continue;
                                                        } else {
                                                            break;
                                                        }
                                                    }

                                                    if (insuranceService.deleteInsurance(Integer.parseInt(insuranceId))) {
                                                        System.out.println("?????? ????????? ??????????????? ?????? ?????????????????????.");
                                                        continue selectDesign;
                                                    } else {
                                                        System.out.println("????????? ?????? ?????? ????????? ????????? ??? ????????????. ?????? ????????? ?????????.");
                                                        continue deleteInsurance;
                                                    }

                                                }
                                            } else if (select3.equals("4")) {
                                                continue design;
                                            } else {
                                                System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                continue;
                                            }
                                        }


                                    } else if (select2.equals("2")) {
                                        continue selectWork;
                                    } else {
                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                        continue;
                                    }

                                }
                            case "q":
                                staff = null;
                                continue main;
                        }

                    case Underwriting:
                        System.out.println("1. ?????? ?????? ??????");
                        System.out.println("q. ????????????");
                        select = sc.nextLine();

                        switch (select) {
                            case "1":
                                underWrite:
                                while (true) {
                                    System.out.println("?????? ?????? ??? ?????? ID??? ????????? ?????????.");
                                    String customerId;

                                    while (true) {
                                        customerId = sc.nextLine();
                                        if (!customerId.matches("[+-]?\\d*(\\.\\d+)?")) {
                                            System.out.println("????????? ????????? ?????????.");
                                            continue;
                                        } else {
                                            break;
                                        }
                                    }
                                    Customer customer = customerService.getCustomer(
                                            Integer.parseInt(customerId));
                                    if (customer == null) {
                                        System.out.println("?????? ????????? ???????????? ????????????.");
                                        continue underWrite;

                                    }

                                    ArrayList<Contract> contracts = contractService.findContract(
                                            Integer.parseInt(customerId));
                                    if (contracts.isEmpty()) {
                                        System.out.println("?????? ????????? ????????? ??? ????????? ????????????.");
                                        continue underWrite;
                                    }

                                    viewContract:
                                    while (true) {
                                        contracts = contractService.findContract(
                                                Integer.parseInt(customerId));
                                        System.out.println(customer.getName() + "?????? ?????? ?????? ??????");
                                        for (Contract contract : contracts) {
                                            Insurance insurance = insuranceService.getInsurance(
                                                    contract.getInsuranceId());
                                            System.out.println(
                                                    "?????? ID : " + contract.getContractId() + " ?????? ID : "
                                                            + insurance.getId() + " ?????? ?????? : "
                                                            + insurance.getName() + " ?????? ?????? ?????? : "
                                                            + contract.isUnderWrite());
                                        }
                                        System.out.println();
                                        System.out.println("1. ?????? ??????");
                                        System.out.println("2. ?????? ??????");

                                        String select1 = sc.nextLine();

                                        if (select1.equals("1")) {
                                            findContract:
                                            while (true) {
                                                System.out.println("?????? ?????? ??? ?????? ID??? ????????? ?????????.");
                                                String contractId;

                                                while (true) {
                                                    contractId = sc.nextLine();
                                                    if (!contractId.matches(
                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                        System.out.println("????????? ????????? ?????????.");
                                                        continue;
                                                    } else {
                                                        break;
                                                    }
                                                }

                                                Contract selectContract = contractService.getContract(
                                                        Integer.parseInt(contractId));

                                                if (selectContract != null) {
                                                    Insurance insurance = insuranceService.getInsurance(
                                                            selectContract.getInsuranceId());
                                                    if (!selectContract.isUnderWrite()) {
                                                        System.out.println("?????? ID : "
                                                                + selectContract.getContractId()
                                                                + " ?????? ID : " + insurance.getId()
                                                                + " ?????? ?????? : " + insurance.getName()
                                                                + " ?????? ?????? : "
                                                                + insurance.getExplanation());
                                                        System.out.println(
                                                                "?????? ????????? ?????? ???????????? ???????????????. ?????? ????????? ?????????????????????????");
                                                        System.out.println("1. ???");
                                                        System.out.println("2. ?????????");
                                                        String select2 = sc.nextLine();

                                                        if (select2.equals("1")) {
                                                            System.out.println(
                                                                    "?????? ID : " + customer.getId()
                                                                            + " ?????? ?????? : "
                                                                            + customer.getName()
                                                                            + " ?????? ?????? : "
                                                                            + customer.getJob().name()
                                                                            + " ?????? ?????? : "
                                                                            + customer.getMedicalHistory().getMyDisease().name());

                                                            System.out.println();
                                                            if (contractService.checkDangerJob(
                                                                    customer)) {
                                                                System.out.println(
                                                                        "?????? ????????? ?????? ???????????? ????????????.");

                                                            } else {
                                                                System.out.println(
                                                                        "?????? ????????? ?????? ??????????????????. ????????? ???????????????");

                                                                if (!contractService.checkAgeDangerJob(
                                                                        customer)) {
                                                                    System.out.println(
                                                                            "??? ????????? ??????????????? ???????????? ????????? ????????? ????????? ??? ????????????.");
                                                                    System.out.println(
                                                                            "?????? : ?????? ????????? ?????? ?????? ??????");
                                                                    continue viewContract;
                                                                }
                                                            }

                                                            if (!contractService.checkDisease(
                                                                    customer)) {
                                                                System.out.println(
                                                                        "??? ????????? ??????????????? ???????????? ????????? ????????? ????????? ??? ????????????.");
                                                                System.out.println(
                                                                        "?????? : ?????? ????????? ???????????? ?????? ??????");
                                                                continue viewContract;
                                                            }

                                                            System.out.println(
                                                                    "?????? ????????? ?????? ????????? ???????????? ????????? ????????????. ??????????????? ?????????????????????! ?????? ????????? ?????????????????????.");

                                                            staffService.addResult(staff);
                                                            contractService.passUnderwrite(selectContract);

                                                            continue viewContract;

                                                        } else if (select2.equals("2")) {
                                                            System.out.println("?????? ????????? ?????????????????????.");
                                                            continue viewContract;
                                                        } else {

                                                        }
                                                    }
                                                } else {
                                                    System.out.println(
                                                            "???????????? ID??? ????????? ???????????? ????????????. ?????? ????????? ?????????.");
                                                    continue findContract;
                                                }
                                            }
                                        } else if (select1.equals("2")) {
                                            continue selectWork;
                                        } else {
                                            System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                            continue viewContract;
                                        }
                                    }

                                }
                            case "q":
                                staff = null;
                                continue main;

                            default:
                                continue;
                        }

                    case Sales:
                        System.out.println("1. ?????? ??????");
                        System.out.println("2. ?????? ?????? ??????");
                        System.out.println("q. ????????????");
                        select = sc.nextLine();

                        switch (select) {
                            case "1":
                                manageCustomer:
                                while (true) {
                                    System.out.println("??? ?????? ??? : " + customerService.totalCustomerCount());
                                    System.out.println(
                                            "??? ?????? ????????? ?????? ??? : " + customerService.thisMonthCustomerCount());
                                    System.out.println(
                                            "????????? ????????? ????????? ??? : " + contractService.unpaidCustomerCount());

                                    System.out.println();
                                    System.out.println("1. ?????? ?????? ????????????");
                                    System.out.println("2. ????????????");

                                    String select1 = sc.nextLine();
                                    if (select1.equals("1")) {
                                        detailCustomer:
                                        while (true) {
                                            ArrayList<Customer> customers = customerService.getTotalCustomer();
                                            if (customers.isEmpty()) {
                                                System.out.println("???????????? ????????? ????????? ????????????.");
                                                continue selectWork;
                                            }
                                            for (Customer customer : customers) {

                                                System.out.print(customer.getId() + ". " + " ?????? : "
                                                        + customer.getName() + " SSN : "
                                                        + customer.getSSN() +
                                                        " ?????? : " + customer.getAddress() + " ???????????? : "
                                                        + customer.getPhoneNumber()
                                                        + " E-mail : " + customer.getEmail()
                                                        + " ????????? ?????? : ");
                                                ArrayList<Insurance> insurances = contractService.getJoinInsurances(customer.getId());

                                                for (Insurance insurance : insurances) {
                                                    System.out.print(insurance.getName() + " ");
                                                }
                                                System.out.println(
                                                        "????????? ?????? ?????? : " + contractService.getPaid(customer.getId()));
                                            }
                                            System.out.println();
                                            System.out.println("1. ?????? ?????? ?????? ??????");
                                            System.out.println("2. ?????? ?????? ?????? ??????");
                                            System.out.println("3. ????????? ?????? ??????");
                                            System.out.println("4. ?????? ??????");

                                            String select2 = sc.nextLine();

                                            if (select2.equals("1")) {
                                                updateCustomer:
                                                while (true) {
                                                    System.out.println("?????? ??? ????????? id??? ????????? ?????????.");
                                                    String customerId;

                                                    while (true) {
                                                        customerId = sc.nextLine();
                                                        if (!customerId.matches(
                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                            System.out.println("????????? ????????? ?????????.");
                                                            continue;
                                                        } else {
                                                            break;
                                                        }
                                                    }

                                                    System.out.println("?????? ?????? ?????? :");
                                                    String address = sc.nextLine();
                                                    System.out.println("?????? ?????? ???????????? :");
                                                    String phoneNum = sc.nextLine();
                                                    System.out.println("?????? ?????? ????????? :");
                                                    String email = sc.nextLine();

                                                    if (customerService.updateCustomer(
                                                            Integer.parseInt(customerId), address,
                                                            phoneNum, email)) {
                                                        System.out.println("?????? ????????? ??????????????? ?????????????????????.");
                                                        continue detailCustomer;
                                                    } else {
                                                        System.out.println(
                                                                "?????? ????????? ?????????????????????. ?????? ????????? ?????????.");
                                                        continue updateCustomer;
                                                    }
                                                }
                                            } else if (select2.equals("2")) {
                                                deleteCustomer:
                                                while (true) {
                                                    System.out.println("?????? ??? ????????? id??? ????????? ?????????.");
                                                    String customerId;

                                                    while (true) {
                                                        customerId = sc.nextLine();
                                                        if (!customerId.matches(
                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                            System.out.println("????????? ????????? ?????????.");
                                                            continue;
                                                        } else {
                                                            break;
                                                        }
                                                    }

                                                    System.out.println(customerService.getCustomerName(
                                                            Integer.parseInt(customerId))
                                                            + "?????? ????????? ?????? ???????????????????");
                                                    System.out.println("1. ???");
                                                    System.out.println("2. ?????????");

                                                    String select3 = sc.nextLine();
                                                    if (select3.equals("1")) {
                                                        if (customerService.deleteCustomer(
                                                                Integer.parseInt(customerId))) {
                                                            System.out.println("??????????????? ?????? ?????????????????????.");
                                                            continue detailCustomer;
                                                        } else {
                                                            System.out.println(
                                                                    "????????? ?????????????????????. ?????? ????????? ?????????.");
                                                            continue;
                                                        }
                                                    } else if (select3.equals("2")) {
                                                        System.out.println("????????? ?????????????????????.");
                                                        continue detailCustomer;
                                                    } else {
                                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                        continue;
                                                    }

                                                }
                                            } else if (select2.equals("3")) {
                                                claimPay:
                                                while (true) {
                                                    ArrayList<Customer> unpaidCustomers = contractService.getUnpaidCustomer();
                                                    if (unpaidCustomers.isEmpty()) {
                                                        System.out.println("????????? ????????? ????????? ????????????.");
                                                        continue detailCustomer;
                                                    }
                                                    for (Customer customer : unpaidCustomers) {
                                                        System.out.print(
                                                                customer.getId() + ". " + " ?????? : "
                                                                        + customer.getName() + " SSN : "
                                                                        + customer.getSSN() +
                                                                        " ?????? : " + customer.getAddress()
                                                                        + " ???????????? : "
                                                                        + customer.getPhoneNumber()
                                                                        + " E-mail : " + customer.getEmail()
                                                                        + " ????????? ????????? : " + contractService.getUnPaidAmount(customer.getId())
                                                                        + " ????????? ?????? : ");

                                                        for (Insurance insurance : contractService.getUnpaidInsurance(customer.getId())) {
                                                            System.out.print(insurance.getName() + " ");
                                                        }
                                                        System.out.println();
                                                    }
                                                    System.out.println("1. ????????? ?????????");
                                                    System.out.println("2. ?????? ??????");

                                                    String select3 = sc.nextLine();

                                                    if (select3.equals("1")) {
                                                        sendEmail:
                                                        while (true) {

                                                            if (contractService.unpaidCustomerCount() == 0) {
                                                                System.out.println("????????? ????????? ?????? ??????????????? ?????? ???????????? ??????????????????. ?????? ??????????????????.");
                                                                continue claimPay;
                                                            }

                                                            System.out.println("????????? ?????? "
                                                                    + contractService.unpaidCustomerCount()
                                                                    + "????????? ???????????? ??????????????????????");

                                                            System.out.println("1. ??????");
                                                            System.out.println("2. ??????");

                                                            String select4 = sc.nextLine();

                                                            if (select4.equals("1")) {
                                                                if (contractService.payCustomer(contractService.getUnpaidCustomer())) {
                                                                    System.out.println(
                                                                            "??????????????? ???????????? ?????????????????????.");
                                                                    continue detailCustomer;
                                                                } else {
                                                                    System.out.println(
                                                                            contractService.unpaidCustomerCount()
                                                                                    + "????????? ????????? ????????? ?????????????????????. ?????? ????????? ?????????.");
                                                                    continue sendEmail;
                                                                }
                                                            } else if (select4.equals("2")) {
                                                                System.out.println("????????? ????????? ?????????????????????.");
                                                                continue detailCustomer;
                                                            } else {
                                                                System.out.println(
                                                                        "????????? ?????? ????????? ????????? ?????????.");
                                                                continue sendEmail;
                                                            }
                                                        }

                                                    } else if (select3.equals("2")) {
                                                        continue detailCustomer;
                                                    } else {
                                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                        continue claimPay;
                                                    }
                                                }
                                            } else if (select2.equals("4")) {
                                                continue manageCustomer;
                                            } else {
                                                System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                continue;
                                            }

                                        }
                                    } else if (select1.equals("2")) {
                                        continue selectWork;
                                    } else {
                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                        continue;
                                    }
                                }
                            case "2":
                                manageContract:
                                while (true) {
                                    if (insuranceService.getInsuranceList() != null) {
                                        for (Insurance insurance : insuranceService.getInsuranceList()) {
                                            if (insurance.isAuthorization()) {
                                                System.out.println(insurance.getId() + ". ?????? : "
                                                        + insurance.getName() + " ?????? : "
                                                        + insurance.getType().name() + " ????????? ??? : "
                                                        + contractService.countContractCustomer(
                                                        insurance.getId()));
                                            }
                                        }
                                    } else {
                                        System.out.println("????????? ????????? ????????????. ????????? ?????? ????????? ?????????.");
                                        continue selectWork;
                                    }
                                    System.out.println();
                                    System.out.println("1. ?????? ??????");
                                    System.out.println("2. ?????? ??????");
                                    System.out.println("3. ?????? ??????");

                                    String select1 = sc.nextLine();
                                    if (select1.equals("1")) {
                                        findContract:
                                        while (true) {
                                            System.out.println("???????????? ????????? ?????? ID??? ????????? ?????????.");
                                            String customerId;

                                            while (true) {
                                                customerId = sc.nextLine();
                                                if (!customerId.matches(
                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                    System.out.println("????????? ????????? ?????????.");
                                                    continue;
                                                } else {
                                                    break;
                                                }
                                            }

                                            if (!contractService.findContract(Integer.parseInt(customerId))
                                                    .isEmpty()) {
                                                selectCancelContract:
                                                while (true) {
                                                    for (Contract contract : contractService.findContract(
                                                            Integer.parseInt(customerId))) {
                                                        System.out.println(
                                                                contract.getContractId()
                                                                        + ".  ?????? ID : "
                                                                        + contract.getCustomerId()
                                                                        + " ?????? ?????? : "
                                                                        + customerService.getCustomerName(
                                                                        contract.getCustomerId())
                                                                        + " ?????? ID : "
                                                                        + contract.getInsuranceId()
                                                                        + " ?????? ?????? : "
                                                                        + insuranceService.getInsuranceName(
                                                                        contract.getInsuranceId()));
                                                    }

                                                    System.out.println();
                                                    System.out.println("1. ?????? ??????");
                                                    System.out.println("2. ?????? ??????");

                                                    String select2 = sc.nextLine();

                                                    if (select2.equals("1")) {
                                                        cancelContract:
                                                        while (true) {
                                                            System.out.println(
                                                                    "?????? ??? ?????? ID??? ????????? ?????????.");
                                                            String contractId;

                                                            while (true) {
                                                                contractId = sc.nextLine();
                                                                if (!contractId.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }

                                                            Contract contract = contractService.getContract(
                                                                    Integer.parseInt(contractId));
                                                            if (contract != null) {
                                                                System.out.println("????????? "
                                                                        + customerService.getCustomerName(
                                                                        contract.getCustomerId())
                                                                        + "?????? "
                                                                        + insuranceService.getInsuranceName(
                                                                        contract.getInsuranceId()) +
                                                                        " ?????? ????????? ?????????????????????????");

                                                                System.out.println("1. ???");
                                                                System.out.println("2. ?????????");

                                                                String select3 = sc.nextLine();
                                                                if (select3.equals("1")) {
                                                                    if (contractService.cancelContract(
                                                                            contract.getContractId())) {
                                                                        System.out.println(
                                                                                date + ", "
                                                                                        + insuranceService.getInsuranceName(
                                                                                        contract.getInsuranceId())
                                                                                        + "?????? ????????? ?????????????????????.");
                                                                        continue selectCancelContract;
                                                                    } else {
                                                                        System.out.println(
                                                                                "????????? ????????? ?????? ????????? ?????? ?????? ???????????????. ?????? ??? ?????? ????????? ?????????.");
                                                                        continue cancelContract;
                                                                    }
                                                                } else if (select3.equals("2")) {
                                                                    System.out.println("?????? ????????? ?????????????????????.");
                                                                    continue selectCancelContract;
                                                                } else {
                                                                    System.out.println(
                                                                            "????????? ?????? ????????? ????????? ?????????.");
                                                                    continue cancelContract;
                                                                }
                                                            } else {
                                                                System.out.println(
                                                                        "????????? ????????? ?????? ??? ????????? ????????? ???????????? ???????????????. ?????? ????????? ?????????.");
                                                                continue cancelContract;
                                                            }
                                                        }
                                                    } else if (select2.equals("2")) {
                                                        continue manageContract;
                                                    } else {
                                                        System.out.println(
                                                                "????????? ?????? ????????? ????????? ?????????.");
                                                        continue selectCancelContract;
                                                    }
                                                }
                                            } else {
                                                System.out.println(
                                                        "?????? ????????? ?????? ????????? ??? ????????????. ?????? ????????? ?????????.");
                                                continue manageContract;
                                            }
                                        }
                                    } else if (select1.equals("2")) {
                                        findInsurance:
                                        while (true) {
                                            System.out.println("????????? ?????? ??? ?????? ID??? ????????? ?????????.");
                                            String insuranceId;

                                            while (true) {
                                                insuranceId = sc.nextLine();
                                                if (!insuranceId.matches(
                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                    System.out.println("????????? ????????? ?????????.");
                                                    continue;
                                                } else {
                                                    break;
                                                }
                                            }

                                            Insurance insurance = insuranceService.getInsurance(
                                                    Integer.parseInt(insuranceId));

                                            if (insurance != null) {
                                                findCustomer:
                                                while (true) {
                                                    Customer customer;
                                                    System.out.println("?????? ???????????? ???????????????????");
                                                    System.out.println("1. ???");
                                                    System.out.println("2. ?????????");
                                                    System.out.println("3. ????????????");

                                                    String select2 = sc.nextLine();
                                                    if (select2.equals("1")) {
                                                        System.out.println("?????? ID??? ????????? ?????????.");

                                                        String customerId;

                                                        while (true) {
                                                            customerId = sc.nextLine();
                                                            if (!customerId.matches(
                                                                    "[+-]?\\d*(\\.\\d+)?")) {
                                                                System.out.println(
                                                                        "????????? ????????? ?????????.");
                                                                continue;
                                                            } else {
                                                                break;
                                                            }
                                                        }

                                                        customer = customerService.getCustomer(
                                                                Integer.parseInt(customerId));

                                                        if (customer == null) {
                                                            System.out.println("???????????? ID??? ?????? ????????? ????????????. ?????? ????????? ?????????.");
                                                            continue findCustomer;
                                                        }

                                                        switch (insurance.getType()) {
                                                            case Car:
                                                                if (customer.getCar() == null) {
                                                                    System.out.println("????????? ?????? : ");
                                                                    String carNum;

                                                                    while (true) {
                                                                        carNum = sc.nextLine();
                                                                        if (!carNum.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println("????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println("????????? ?????? : ");
                                                                    String carYear;

                                                                    while (true) {
                                                                        carYear = sc.nextLine();
                                                                        if (!carYear.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println("????????? ????????? : ");
                                                                    String carDisplacement;

                                                                    while (true) {
                                                                        carDisplacement = sc.nextLine();
                                                                        if (!carDisplacement.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println("????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println("????????? ?????? : ");
                                                                    String carPrice;

                                                                    while (true) {
                                                                        carPrice = sc.nextLine();
                                                                        if (!carPrice.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    customerService.setCustomerCar(
                                                                            customer,
                                                                            Integer.parseInt(
                                                                                    carNum),
                                                                            Integer.parseInt(
                                                                                    carYear),
                                                                            Integer.parseInt(
                                                                                    carDisplacement),
                                                                            Integer.parseInt(
                                                                                    carPrice));
                                                                }
                                                                break;
                                                            case Fire:
                                                                if (customer.getHouse() == null) {
                                                                    System.out.println(
                                                                            "?????? ?????? : 1. ????????? / 2. ?????? / 3. ????????????");
                                                                    String houseType;

                                                                    while (true) {
                                                                        houseType = sc.nextLine();
                                                                        if (!houseType.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    System.out.println(
                                                                            "?????? ?????? : ");
                                                                    String housePrice;
                                                                    while (true) {
                                                                        housePrice = sc.nextLine();
                                                                        if (!housePrice.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    customerService.setCustomerHouse(
                                                                            customer,
                                                                            Integer.parseInt(
                                                                                    houseType),
                                                                            Integer.parseInt(
                                                                                    housePrice));
                                                                }
                                                                break;
                                                            case Sea:
                                                                if (customer.getShip() == null) {
                                                                    System.out.println(
                                                                            "?????? ?????? : ");
                                                                    String shipNum;

                                                                    while (true) {
                                                                        shipNum = sc.nextLine();
                                                                        if (!shipNum.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    System.out.println(
                                                                            "?????? ?????? : ");
                                                                    String shipYear;

                                                                    while (true) {
                                                                        shipYear = sc.nextLine();
                                                                        if (!shipYear.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println(
                                                                            "?????? ?????? : ");
                                                                    String shipPrice;

                                                                    while (true) {
                                                                        shipPrice = sc.nextLine();
                                                                        if (!shipPrice.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println(
                                                                            "?????? ?????? : 1. ?????? ?????? / 2. ??????????????? ");
                                                                    String shipType;

                                                                    while (true) {
                                                                        shipType = sc.nextLine();
                                                                        if (!shipType.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    customerService.setCustomerSea(
                                                                            customer,
                                                                            Integer.parseInt(
                                                                                    shipNum),
                                                                            Integer.parseInt(
                                                                                    shipYear),
                                                                            Integer.parseInt(
                                                                                    shipPrice),
                                                                            Integer.parseInt(
                                                                                    shipType));
                                                                }
                                                                break;
                                                        }

                                                        if (contractService.signContract(
                                                                insurance.getId(),
                                                                customer, staff.getId())) {
                                                            System.out.println(
                                                                    "????????? ????????? ?????????????????????. ?????? ?????? ??? ?????? ?????? ????????? ???????????????.");
                                                            continue manageContract;
                                                        } else {
                                                            System.out.println(
                                                                    "????????? ?????? ????????? ?????? ????????? ?????????????????????. ????????? ?????? ??????????????????.");
                                                            continue findInsurance;
                                                        }


                                                    } else if (select2.equals("2")) {
                                                        createCustomer:
                                                        while (true) {
                                                            System.out.println(
                                                                    "????????? ???????????? ????????? ????????? ????????? ?????????.");

                                                            customer = new Customer();

                                                            System.out.println("?????? ?????? : ");
                                                            String customerName = sc.nextLine();
                                                            System.out.println("?????? ?????????????????? : ");
                                                            String customerSSN = sc.nextLine();
                                                            System.out.println("?????? ?????? : ");
                                                            String customerAddress = sc.nextLine();
                                                            System.out.println("?????? ???????????? : ");
                                                            String customerPhoneNum = sc.nextLine();
                                                            System.out.println("?????? ????????? : ");
                                                            String customerEmail = sc.nextLine();
                                                            System.out.println("?????? ???????????? : ");
                                                            String customerAccount = sc.nextLine();
                                                            System.out.println(
                                                                    "?????? ?????? : 1. ?????? / 2. ??????");
                                                            String customerSex;
                                                            while (true) {
                                                                customerSex = sc.nextLine();
                                                                if (!customerSex.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }
                                                            System.out.println("?????? ?????? : ");
                                                            String customerAge;
                                                            while (true) {
                                                                customerAge = sc.nextLine();
                                                                if (!customerAge.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }

                                                            System.out.println(
                                                                    "?????? ?????? : 1. ????????? / 2. ????????? / 3. ????????? / 4. ???????????? / 5. ??????");
                                                            String customerJob;

                                                            while (true) {
                                                                customerJob = sc.nextLine();
                                                                if (!customerJob.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }

                                                            System.out.println(
                                                                    "?????? ??????: 1. ??? / 2. ?????? / 3. ?????? / 4. ?????? / 5. ??????");
                                                            String customerDisease;
                                                            while (true) {
                                                                customerDisease = sc.nextLine();
                                                                if (!customerDisease.matches(
                                                                        "[+-]?\\d*(\\.\\d+)?")) {
                                                                    System.out.println(
                                                                            "????????? ????????? ?????????.");
                                                                    continue;
                                                                } else {
                                                                    break;
                                                                }
                                                            }
                                                            if (!customerDisease.equals("5")) {
                                                                System.out.println("?????? ??????: ");
                                                                String customerHistoryYear;

                                                                while (true) {
                                                                    customerHistoryYear = sc.nextLine();
                                                                    if (!customerHistoryYear.matches(
                                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                                        System.out.println(
                                                                                "????????? ????????? ?????????.");
                                                                        continue;
                                                                    } else {
                                                                        break;
                                                                    }
                                                                }

                                                                System.out.println(
                                                                        "?????? ??????: 1. ?????? / 2. ?????????");
                                                                String customerCureComplete;

                                                                while (true) {
                                                                    customerCureComplete = sc.nextLine();
                                                                    if (!customerCureComplete.matches(
                                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                                        System.out.println(
                                                                                "????????? ????????? ?????????.");
                                                                        continue;
                                                                    } else {
                                                                        break;
                                                                    }
                                                                }

                                                                customer = customerService.joinCustomer(
                                                                        customerName, customerSSN,
                                                                        customerAddress,
                                                                        customerPhoneNum,
                                                                        customerEmail,
                                                                        customerAccount,
                                                                        Integer.parseInt(
                                                                                customerAge),
                                                                        Integer.parseInt(
                                                                                customerSex),
                                                                        Integer.parseInt(
                                                                                customerJob)
                                                                        , Integer.parseInt(
                                                                                customerDisease),
                                                                        Integer.parseInt(
                                                                                customerHistoryYear),
                                                                        Integer.parseInt(
                                                                                customerCureComplete));
                                                            } else {
                                                                customer = customerService.joinCustomer(
                                                                        customerName, customerSSN,
                                                                        customerAddress,
                                                                        customerPhoneNum,
                                                                        customerEmail,
                                                                        customerAccount,
                                                                        Integer.parseInt(
                                                                                customerAge),
                                                                        Integer.parseInt(
                                                                                customerSex),
                                                                        Integer.parseInt(
                                                                                customerJob)
                                                                        , Integer.parseInt(
                                                                                customerDisease), 0, 0);
                                                            }

                                                            switch (insurance.getType()) {
                                                                case Car:
                                                                    System.out.println(
                                                                            "????????? ?????? : ");
                                                                    String carNum;

                                                                    while (true) {
                                                                        carNum = sc.nextLine();
                                                                        if (!carNum.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println(
                                                                            "????????? ?????? : ");
                                                                    String carYear;

                                                                    while (true) {
                                                                        carYear = sc.nextLine();
                                                                        if (!carYear.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println(
                                                                            "????????? ????????? : ");
                                                                    String carDisplacement;

                                                                    while (true) {
                                                                        carDisplacement = sc.nextLine();
                                                                        if (!carDisplacement.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println(
                                                                            "????????? ?????? : ");
                                                                    String carPrice;

                                                                    while (true) {
                                                                        carPrice = sc.nextLine();
                                                                        if (!carPrice.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    customerService.setCustomerCar(
                                                                            customer,
                                                                            Integer.parseInt(
                                                                                    carNum),
                                                                            Integer.parseInt(
                                                                                    carYear),
                                                                            Integer.parseInt(
                                                                                    carDisplacement),
                                                                            Integer.parseInt(
                                                                                    carPrice));
                                                                    break;
                                                                case Fire:
                                                                    System.out.println(
                                                                            "?????? ?????? : 1. ????????? / 2. ?????? / 3. ????????????");
                                                                    String houseType;

                                                                    while (true) {
                                                                        houseType = sc.nextLine();
                                                                        if (!houseType.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    System.out.println(
                                                                            "?????? ?????? : ");
                                                                    String housePrice;
                                                                    while (true) {
                                                                        housePrice = sc.nextLine();
                                                                        if (!housePrice.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    customerService.setCustomerHouse(
                                                                            customer,
                                                                            Integer.parseInt(
                                                                                    houseType),
                                                                            Integer.parseInt(
                                                                                    housePrice));
                                                                    break;
                                                                case Sea:
                                                                    System.out.println(
                                                                            "?????? ?????? : ");
                                                                    String shipNum;

                                                                    while (true) {
                                                                        shipNum = sc.nextLine();
                                                                        if (!shipNum.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    System.out.println(
                                                                            "?????? ?????? : ");
                                                                    String shipYear;

                                                                    while (true) {
                                                                        shipYear = sc.nextLine();
                                                                        if (!shipYear.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println(
                                                                            "?????? ?????? : ");
                                                                    String shipPrice;

                                                                    while (true) {
                                                                        shipPrice = sc.nextLine();
                                                                        if (!shipPrice.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }
                                                                    System.out.println(
                                                                            "?????? ?????? : 1. ?????? ?????? / 2. ??????????????? ");
                                                                    String shipType;

                                                                    while (true) {
                                                                        shipType = sc.nextLine();
                                                                        if (!shipType.matches(
                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                            System.out.println(
                                                                                    "????????? ????????? ?????????.");
                                                                            continue;
                                                                        } else {
                                                                            break;
                                                                        }
                                                                    }

                                                                    customerService.setCustomerSea(
                                                                            customer,
                                                                            Integer.parseInt(
                                                                                    shipNum),
                                                                            Integer.parseInt(
                                                                                    shipYear),
                                                                            Integer.parseInt(
                                                                                    shipPrice),
                                                                            Integer.parseInt(
                                                                                    shipType));
                                                                    break;
                                                            }

                                                            if (contractService.signContract(
                                                                    insurance.getId(),
                                                                    customer, staff.getId())) {
                                                                staffService.addResult(staff);
                                                                System.out.println(
                                                                        "????????? ????????? ?????????????????????. ?????? ?????? ??? ?????? ?????? ????????? ???????????????.");
                                                                continue manageContract;
                                                            } else {
                                                                System.out.println(
                                                                        "????????? ?????? ????????? ?????? ????????? ?????????????????????. ????????? ?????? ??????????????????.");
                                                                continue findInsurance;
                                                            }


                                                        }
                                                    } else if (select2.equals("3")) {
                                                        continue findInsurance;
                                                    } else {
                                                        System.out.println(
                                                                "????????? ?????? ????????? ????????? ?????????.");
                                                        continue findCustomer;
                                                    }


                                                }
                                            } else {
                                                System.out.println(
                                                        "???????????? ID??? ????????? ?????? ??? ????????????. ?????? ????????? ?????????.");
                                                continue findInsurance;
                                            }


                                        }
                                    } else if (select1.equals("3")) {
                                        continue selectWork;
                                    } else {
                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                        continue;
                                    }

                                }
                            case "q":
                                staff = null;
                                continue main;
                            default:
                                continue;
                        }

                    case Human:
                        System.out.println("1. ?????? ??????");
                        System.out.println("q. ????????????");
                        select = sc.nextLine();

                        switch (select) {
                            case "1":
                                manageStaff:
                                while (true) {
                                    ArrayList<Staff> staffs = new ArrayList<>();
                                    staffs = staffService.getTotalStaff();
                                    if (!staffs.isEmpty()) {
                                        for (Staff getStaff : staffs) {
                                            staffService.calculateSalary(
                                                    getStaff.getId(), staff);
                                            System.out.println(
                                                    "?????? : " + getStaff.getDepartment().name()
                                                            + " ?????? ?????? : " + getStaff.getId()
                                                            + " ?????? ?????? : " + getStaff.getName()
                                                            + " ?????? ?????? : "
                                                            + getStaff.getJoinDate());
                                        }
                                        System.out.println("1. ?????? ??????");
                                        System.out.println("2. ?????? ??????");
                                        System.out.println("3. ?????? ??????");
                                        System.out.println("4. ?????? ??????");
                                        System.out.println("5. ?????? ??????");

                                        String select2 = sc.nextLine();
                                        if (select2.equals("1")) {
                                            findStaff:
                                            while (true) {

                                                System.out.println(
                                                        "????????? ??? ????????? ID??? ????????? ?????????.");
                                                String staffId;

                                                while (true) {
                                                    staffId = sc.nextLine();
                                                    if (!staffId.matches(
                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                        System.out.println("????????? ????????? ?????????.");
                                                        continue;
                                                    } else {
                                                        break;
                                                    }
                                                }

                                                Staff findStaff = staffService.getStaff(Integer.parseInt(staffId));

                                                System.out.println(
                                                        " ?????? ?????? : " + findStaff.getId()
                                                                + " ?????? ?????? : " + findStaff.getName()
                                                                + " ?????? : "
                                                                + findStaff.getDepartment().name()
                                                                + "?????????????????? : " + findStaff.getSSN()
                                                                + " ????????? : " + findStaff.getEmail()
                                                                + " ?????? ?????? : "
                                                                + findStaff.getPhoneNum()
                                                                + " ?????? ?????? : "
                                                                + findStaff.getJoinDate());

                                                continue manageStaff;
                                            }


                                        } else if (select2.equals("2")) {
                                            managementSalary:
                                            while (true) {
                                                System.out.println(
                                                        "????????? ????????? ????????? ID??? ????????? ?????????.");
                                                String staffId;

                                                while (true) {
                                                    staffId = sc.nextLine();
                                                    if (!staffId.matches(
                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                        System.out.println("????????? ????????? ?????????.");
                                                        continue;
                                                    } else {
                                                        break;
                                                    }
                                                }

                                                Staff manageStaff = staffService.getStaff(
                                                        Integer.parseInt(staffId));

                                                if (manageStaff == null) {
                                                    System.out.println(
                                                            "???????????????. ?????? ????????? ?????? ????????? ????????? ????????? ??? ????????????. ?????? ??? ?????? ??????????????????.");
                                                    continue managementSalary;
                                                }

                                                System.out.println(
                                                        "?????? : " + manageStaff.getPosition()
                                                                .name() + " ?????? ??????: "
                                                                + staffService.calculateWorkDate(
                                                                manageStaff.getId())
                                                                + " ????????? ?????? : "
                                                                + manageStaff.getResult()
                                                                + " ?????? ?????? : "
                                                                + manageStaff.getTotalSalary());

                                                System.out.println("1. ?????? ????????????");
                                                System.out.println("2. ????????????");
                                                String select3 = sc.nextLine();
                                                if (select3.equals("1")) {
                                                    changePosition:
                                                    while (true) {
                                                        System.out.println(
                                                                "?????? ?????? ????????? ????????? ?????????.");
                                                        System.out.println(
                                                                "?????? 1. ????????? / 2. ?????? / 3. ?????? / 4. ?????? / 5. ?????? / 6. ??????");

                                                        String position;

                                                        while (true) {
                                                            position = sc.nextLine();
                                                            if (!position.matches(
                                                                    "[+-]?\\d*(\\.\\d+)?")) {
                                                                System.out.println(
                                                                        "????????? ????????? ?????????.");
                                                                continue;
                                                            } else {
                                                                break;
                                                            }
                                                        }
                                                        if (Staff.Position.values()[Integer.parseInt(position)] != null) {
                                                            System.out.println("?????? ?????? ????????? " +
                                                                    Staff.Position.values()[Integer.parseInt(position)]
                                                                    + "??? ?????????????");


                                                            System.out.println("1. ???");
                                                            System.out.println("2. ?????????");

                                                            String select4 = sc.next();

                                                            if (select4.equals("1")) {
                                                                if (staffService.changePosition(
                                                                        manageStaff,
                                                                        Integer.parseInt(position), staff)) {
                                                                    System.out.println("????????? ?????????????????????. ????????? ?????? ?????? ????????? ???????????????.");
                                                                    continue manageStaff;
                                                                } else {
                                                                    System.out.println("????????? ?????? ????????? ?????? ?????? ????????? ?????????????????????. ?????? ????????? ?????????.");
                                                                    continue manageStaff;
                                                                }

                                                            } else if (select4.equals("2")) {
                                                                System.out.println("?????? ????????? ?????????????????????.");
                                                                continue manageStaff;
                                                            } else {
                                                                System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                                continue changePosition;
                                                            }


                                                        }

                                                    }
                                                } else if (select3.equals("2")) {
                                                    continue manageStaff;
                                                } else {
                                                    continue managementSalary;
                                                }
                                            }


                                        } else if (select2.equals("3")) {
                                            changeDepartment:
                                            while (true) {
                                                System.out.println("????????? ????????? ????????? ID??? ????????? ?????????.");


                                                String staffId;

                                                while (true) {
                                                    staffId = sc.nextLine();
                                                    if (!staffId.matches(
                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                        System.out.println("????????? ????????? ?????????.");
                                                        continue;
                                                    } else {
                                                        break;
                                                    }
                                                }

                                                System.out.println("????????? ????????? ????????? ?????????.");
                                                System.out.println("1. ?????? ?????????");
                                                System.out.println("2. ?????? ?????????");
                                                System.out.println("3. ?????? ?????????");
                                                System.out.println("4. ?????? ?????????");
                                                System.out.println("5. ?????? ?????????");

                                                String changeDepartment;

                                                while (true) {
                                                    changeDepartment = sc.nextLine();
                                                    if (!changeDepartment.matches(
                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                        System.out.println("????????? ????????? ?????????.");
                                                        continue;
                                                    } else {
                                                        break;
                                                    }
                                                }

                                                if (staffService.updateDepartment(
                                                        Integer.parseInt(staffId),
                                                        Integer.parseInt(changeDepartment))) {
                                                    System.out.println(
                                                            "??????????????? ????????? ?????????????????????.");
                                                    continue manageStaff;
                                                } else {
                                                    System.out.println(
                                                            "????????? ?????? ????????? ?????? ?????? ????????? ?????????????????????. ?????? ??????????????????.");
                                                    continue changeDepartment;
                                                }
                                            }

                                        } else if (select2.equals("4")) {
                                            fireStaff:
                                            while (true) {
                                                System.out.println("???????????? ????????? ID??? ????????? ?????????.");
                                                String staffId;

                                                while (true) {
                                                    staffId = sc.nextLine();
                                                    if (!staffId.matches(
                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                        System.out.println("????????? ????????? ?????????.");
                                                        continue;
                                                    } else {
                                                        break;
                                                    }
                                                }

                                                System.out.println(
                                                        "????????? ????????????????????????? ?????? ????????? ????????? ??????????????? ???????????????.");
                                                System.out.println("1. ???");
                                                System.out.println("2. ?????????");
                                                String select3 = sc.nextLine();
                                                if (select3.equals("1")) {
                                                    staffService.fireStaff(
                                                            Integer.parseInt(staffId));
                                                    System.out.println("????????? ?????????????????????.");
                                                    continue manageStaff;
                                                } else if (select3.equals("2")) {
                                                    System.out.println("????????? ?????????????????????.");
                                                    continue manageStaff;
                                                } else {
                                                    System.out.println(
                                                            "????????? ?????? ????????? ????????? ?????????.");
                                                    continue fireStaff;
                                                }
                                            }
                                        } else if (select2.equals("5")) {
                                            continue selectWork;
                                        } else {
                                            System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                            continue manageStaff;
                                        }
                                    } else {
                                        System.out.println(
                                                "???????????????. ?????? ????????? ????????? ????????? ??? ????????????. ?????? ??? ?????? ????????? ?????????.");
                                        continue manageStaff;
                                    }

                                }
                            case "q":
                                staff = null;
                                continue main;
                            default:
                                continue;
                        }
                    case CompensationManagement:
                        System.out.println("1. ?????? ??????");
                        System.out.println("q. ????????????");
                        select = sc.nextLine();

                        switch (select) {
                            case "1":
                                compensationManage:
                                while (true) {
                                    System.out.println("1. ?????? ????????????");
                                    System.out.println("2. ?????? ??????");

                                    String select1 = sc.nextLine();
                                    if (select1.equals("1")) {
                                        findContract:
                                        while (true) {
                                            System.out.println("?????? ID : ");
                                            String customerId;

                                            while (true) {
                                                customerId = sc.nextLine();
                                                if (!customerId.matches("[+-]?\\d*(\\.\\d+)?")) {
                                                    System.out.println("????????? ????????? ?????????.");
                                                    continue;
                                                } else {
                                                    break;
                                                }
                                            }

                                            ArrayList<Contract> findContract = contractService.findInsuranceContracts(
                                                    Integer.parseInt(customerId));
                                            if (!findContract.isEmpty()) {
                                                for (Contract contract : findContract) {
                                                    Insurance insurance = insuranceService.getInsurance(
                                                            contract.getInsuranceId());
                                                    System.out.println(
                                                            "?????? ID : " + contract.getContractId()
                                                                    + " ?????? ID : " + insurance.getId()
                                                                    + ". ?????? ?????? : " + insurance.getName()
                                                                    + " ?????? ?????? : " + insurance.getType()
                                                                    .name());
                                                }
                                                System.out.println("?????? ?????? ??? ?????? ID??? ????????? ?????????.");
                                                String contractId;


                                                while (true) {
                                                    contractId = sc.nextLine();
                                                    if (!contractId.matches(
                                                            "[+-]?\\d*(\\.\\d+)?")) {
                                                        System.out.println("????????? ????????? ?????????.");
                                                        continue;
                                                    } else {
                                                        break;
                                                    }
                                                }
                                                Contract contract = contractService.getContract(
                                                        Integer.parseInt(contractId));

                                                if (contract == null) {
                                                    System.out.println(
                                                            "???????????? ????????? ???????????? ??????????????????. ?????? ????????? ?????????.");
                                                    continue findContract;
                                                }


                                                Insurance insurance = insuranceService.getInsurance(
                                                        contract.getInsuranceId());


                                                Customer customer = customerService.getCustomer(
                                                        Integer.parseInt(customerId));

                                                judgementSubject:
                                                while (true) {
                                                    System.out.println("?????? ????????? ????????? ????????? ?????????.");
                                                    for (AccidentSubjectIndemnification accidentSubjectIndemnification : AccidentSubjectIndemnification.values()) {
                                                        System.out.println(
                                                                accidentSubjectIndemnification.ordinal()
                                                                        + 1 + ". "
                                                                        + accidentSubjectIndemnification.getExplanation());
                                                    }
                                                    String subject;

                                                    while (true) {
                                                        subject = sc.nextLine();
                                                        if (!subject.matches(
                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                            System.out.println("????????? ????????? ?????????.");
                                                            continue;
                                                        } else {
                                                            break;
                                                        }
                                                    }

                                                    if (subject.equals("1") || subject.equals("2")
                                                            || subject.equals("3")) {
                                                        if (contractService.judgeSubjectIndemnification(
                                                                Integer.parseInt(subject))) {
                                                            judgementIndemnification:
                                                            while (true) {
                                                                String indemnification;
                                                                System.out.println(
                                                                        "?????? ?????? ????????? ????????? ?????????.");
                                                                switch (insurance.getType()) {
                                                                    case Car:
                                                                        for (CarAccidentCauseIndemnification carAccidentCauseIndemnification : CarAccidentCauseIndemnification.values()) {
                                                                            System.out.println(
                                                                                    carAccidentCauseIndemnification.ordinal()
                                                                                            + 1 + ". "
                                                                                            + carAccidentCauseIndemnification.getExplanation());
                                                                        }
                                                                        while (true) {
                                                                            indemnification = sc.nextLine();
                                                                            if (!indemnification.matches(
                                                                                    "[+-]?\\d*(\\.\\d+)?")) {
                                                                                System.out.println(
                                                                                        "????????? ????????? ?????????.");
                                                                                continue;
                                                                            } else {
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (indemnification.equals("1") || indemnification.equals("2") || indemnification.equals("3")
                                                                                || indemnification.equals("4") || indemnification.equals("5") || indemnification.equals("6") || indemnification.equals("7")) {
                                                                            if (contractService.judgeCarIndemnification(
                                                                                    Integer.parseInt(
                                                                                            indemnification))) {
                                                                                calculateCompensation:
                                                                                while (true) {
                                                                                    System.out.println(
                                                                                            "??? ?????? (0 ~ 100) : ");
                                                                                    String carDamage;

                                                                                    while (true) {
                                                                                        carDamage = sc.nextLine();
                                                                                        if (!carDamage.matches(
                                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                                            System.out.println(
                                                                                                    "????????? ????????? ?????????.");
                                                                                            continue;
                                                                                        } else {
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                    System.out.println(
                                                                                            "?????? ?????? (0 ~ 100) ");
                                                                                    String humanDamage;

                                                                                    while (true) {
                                                                                        humanDamage = sc.nextLine();
                                                                                        if (!humanDamage.matches(
                                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                                            System.out.println(
                                                                                                    "????????? ????????? ?????????.");
                                                                                            continue;
                                                                                        } else {
                                                                                            break;
                                                                                        }
                                                                                    }


                                                                                    System.out.println("???????????? ????????? ????????? ????????? ???????????????????");
                                                                                    System.out.println("1. ???");
                                                                                    System.out.println("2. ?????????");

                                                                                    String select3 = sc.nextLine();

                                                                                    if (select3.equals("1")) {
                                                                                        contractService.compensation(contract.getContractId(), Integer.parseInt(humanDamage),
                                                                                                0, 0, Integer.parseInt(carDamage), 0, 0);
                                                                                        staffService.addResult(staff);
                                                                                        break calculateCompensation;
                                                                                    } else if (select3.equals("2")) {
                                                                                        System.out.println("????????? ????????? ?????????????????????.");
                                                                                        continue compensationManage;
                                                                                    } else {
                                                                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                                                        continue;
                                                                                    }


                                                                                }
                                                                                break;
                                                                            } else {
                                                                                System.out.println(
                                                                                        "???????????????. "
                                                                                                + CarAccidentCauseIndemnification.values()[
                                                                                                Integer.parseInt(
                                                                                                        indemnification)
                                                                                                        - 1].getExplanation()
                                                                                                + "??? ?????? ????????? ????????? ??????????????? ?????? ?????? ???????????????.");
                                                                                continue findContract;
                                                                            }
                                                                        } else {
                                                                            System.out.println(
                                                                                    "????????? ?????? ????????? ????????? ?????????.");
                                                                            continue judgementIndemnification;
                                                                        }
                                                                    case Sea:
                                                                        for (SeaAccidentCauseIndemnification seaAccidentCauseIndemnification : SeaAccidentCauseIndemnification.values()) {
                                                                            System.out.println(
                                                                                    seaAccidentCauseIndemnification.ordinal()
                                                                                            + 1 + ". "
                                                                                            + seaAccidentCauseIndemnification.getExplanation());
                                                                        }
                                                                        while (true) {
                                                                            indemnification = sc.nextLine();
                                                                            if (!indemnification.matches(
                                                                                    "[+-]?\\d*(\\.\\d+)?")) {
                                                                                System.out.println(
                                                                                        "????????? ????????? ?????????.");
                                                                                continue;
                                                                            } else {
                                                                                break;
                                                                            }
                                                                        }

                                                                        if (indemnification.equals(
                                                                                "1")
                                                                                || indemnification.equals(
                                                                                "2")
                                                                                || indemnification.equals(
                                                                                "3")
                                                                                || indemnification.equals(
                                                                                "4")) {
                                                                            if (contractService.judgeSeaIndemnification(
                                                                                    Integer.parseInt(
                                                                                            indemnification))) {
                                                                                calculateCompensation:
                                                                                while (true) {
                                                                                    System.out.println(
                                                                                            "?????? ?????? (0 ~ 100) : ");
                                                                                    String generalDamage;

                                                                                    while (true) {
                                                                                        generalDamage = sc.nextLine();
                                                                                        if (!generalDamage.matches(
                                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                                            System.out.println(
                                                                                                    "????????? ????????? ?????????.");
                                                                                            continue;
                                                                                        } else {
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                    System.out.println(
                                                                                            "?????? ?????? (0 ~ 100) ");
                                                                                    String revenueDamage;

                                                                                    while (true) {
                                                                                        revenueDamage = sc.nextLine();
                                                                                        if (!revenueDamage.matches(
                                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                                            System.out.println(
                                                                                                    "????????? ????????? ?????????.");
                                                                                            continue;
                                                                                        } else {
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                    System.out.println("???????????? ????????? ????????? ????????? ???????????????????");
                                                                                    System.out.println("1. ???");
                                                                                    System.out.println("2. ?????????");

                                                                                    String select3 = sc.nextLine();

                                                                                    if (select3.equals("1")) {
                                                                                        contractService.compensation(
                                                                                                contract.getContractId(),
                                                                                                0, 0, 0, 0,
                                                                                                Integer.parseInt(
                                                                                                        generalDamage),
                                                                                                Integer.parseInt(
                                                                                                        revenueDamage));
                                                                                        staffService.addResult(staff);
                                                                                        break calculateCompensation;
                                                                                    } else if (select3.equals("2")) {
                                                                                        System.out.println("????????? ????????? ?????????????????????.");
                                                                                        continue compensationManage;
                                                                                    } else {
                                                                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                                                        continue;
                                                                                    }


                                                                                }
                                                                                break;
                                                                            } else {
                                                                                System.out.println(
                                                                                        "???????????????. "
                                                                                                + SeaAccidentCauseIndemnification.values()[
                                                                                                Integer.parseInt(
                                                                                                        indemnification)
                                                                                                        - 1].getExplanation()
                                                                                                + "??? ?????? ????????? ????????? ??????????????? ?????? ?????? ???????????????.");
                                                                                continue findContract;
                                                                            }
                                                                        } else {
                                                                            System.out.println(
                                                                                    "????????? ?????? ????????? ????????? ?????????.");
                                                                            continue judgementIndemnification;
                                                                        }
                                                                    case Fire:
                                                                        for (FireAccidentCauseIndemnification fireAccidentCauseIndemnification : FireAccidentCauseIndemnification.values()) {
                                                                            System.out.println(
                                                                                    fireAccidentCauseIndemnification.ordinal()
                                                                                            + 1 + ". "
                                                                                            + fireAccidentCauseIndemnification.getExplanation());
                                                                        }
                                                                        while (true) {
                                                                            indemnification = sc.nextLine();
                                                                            if (!indemnification.matches(
                                                                                    "[+-]?\\d*(\\.\\d+)?")) {
                                                                                System.out.println(
                                                                                        "????????? ????????? ?????????.");
                                                                                continue;
                                                                            } else {
                                                                                break;
                                                                            }
                                                                        }

                                                                        if (indemnification.equals(
                                                                                "1")
                                                                                || indemnification.equals(
                                                                                "2")
                                                                                || indemnification.equals(
                                                                                "3")
                                                                                || indemnification.equals(
                                                                                "4")) {
                                                                            if (contractService.judgeFireIndemnification(
                                                                                    Integer.parseInt(
                                                                                            indemnification))) {
                                                                                calculateCompensation:
                                                                                while (true) {
                                                                                    System.out.println(
                                                                                            "?????? ?????? ?????? ?????? (0 ~ 100) : ");
                                                                                    String buildingDamage;

                                                                                    while (true) {
                                                                                        buildingDamage = sc.nextLine();
                                                                                        if (!buildingDamage.matches(
                                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                                            System.out.println(
                                                                                                    "????????? ????????? ?????????.");
                                                                                            continue;
                                                                                        } else {
                                                                                            break;
                                                                                        }
                                                                                    }

                                                                                    System.out.println(
                                                                                            "?????? ?????? (0 ~ 100) ");
                                                                                    String humanDamage;

                                                                                    while (true) {
                                                                                        humanDamage = sc.nextLine();
                                                                                        if (!humanDamage.matches(
                                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                                            System.out.println(
                                                                                                    "????????? ????????? ?????????.");
                                                                                            continue;
                                                                                        } else {
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                    System.out.println(
                                                                                            "?????? ?????? (0 ~ 100) ");
                                                                                    String surroundingDamage;

                                                                                    while (true) {
                                                                                        surroundingDamage = sc.nextLine();
                                                                                        if (!surroundingDamage.matches(
                                                                                                "[+-]?\\d*(\\.\\d+)?")) {
                                                                                            System.out.println(
                                                                                                    "????????? ????????? ?????????.");
                                                                                            continue;
                                                                                        } else {
                                                                                            break;
                                                                                        }
                                                                                    }

                                                                                    System.out.println("???????????? ????????? ????????? ????????? ???????????????????");
                                                                                    System.out.println("1. ???");
                                                                                    System.out.println("2. ?????????");

                                                                                    String select3 = sc.nextLine();

                                                                                    if (select3.equals("1")) {
                                                                                        contractService.compensation(
                                                                                                contract.getContractId(), Integer.parseInt(humanDamage),
                                                                                                Integer.parseInt(buildingDamage), Integer.parseInt(surroundingDamage),
                                                                                                0, 0, 0);
                                                                                        staffService.addResult(staff);
                                                                                        break calculateCompensation;
                                                                                    } else if (select3.equals("2")) {
                                                                                        System.out.println("????????? ????????? ?????????????????????.");
                                                                                        continue compensationManage;
                                                                                    } else {
                                                                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                                                        continue;
                                                                                    }


                                                                                }
                                                                            } else {
                                                                                System.out.println(
                                                                                        "???????????????. "
                                                                                                + FireAccidentCauseIndemnification.values()[
                                                                                                Integer.parseInt(
                                                                                                        indemnification)
                                                                                                        - 1].getExplanation()
                                                                                                + "??? ?????? ????????? ????????? ??????????????? ?????? ?????? ???????????????.");
                                                                                continue findContract;
                                                                            }
                                                                        } else {
                                                                            System.out.println(
                                                                                    "????????? ?????? ????????? ????????? ?????????.");
                                                                            continue judgementIndemnification;
                                                                        }
                                                                }

                                                                Contract updateContract = contractService.getContract(contract.getContractId());
                                                                System.out.println(
                                                                        customer.getName()
                                                                                + "?????? ??? ?????? ????????? "
                                                                                + updateContract.getCompensationAmount()
                                                                                + "??? ?????????.");
                                                                continue compensationManage;
                                                            }
                                                        } else {
                                                            System.out.println("???????????????. "
                                                                    + AccidentSubjectIndemnification.values()[
                                                                    Integer.parseInt(subject)
                                                                            - 1].getExplanation()
                                                                    + "??? ?????? ????????? ????????? ??????????????? ?????? ?????? ???????????????.");
                                                            continue findContract;
                                                        }
                                                    } else {
                                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                                        continue judgementSubject;
                                                    }
                                                }

                                            } else {
                                                System.out.println(
                                                        "????????? ????????? ????????????. ?????? ?????? ?????? ??? ?????? ????????? ?????????.");
                                                continue compensationManage;
                                            }
                                        }
                                    } else if (select1.equals("2")) {
                                        continue selectWork;
                                    } else {
                                        System.out.println("????????? ?????? ????????? ????????? ?????????.");
                                        continue compensationManage;
                                    }
                                }
                            case "q":
                                staff = null;
                                continue main;
                            default:
                        }


                }

            }

        }
    }
}
