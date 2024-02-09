package org.musketeers.service;

import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
public class CompanyService extends ServiceManager<Company, Long> {
    private final CompanyRepository companyRepository;
    private final JwtTokenManager jwtTokenManager;

    public CompanyService(CompanyRepository companyRepository,JwtTokenManager jwtTokenManager) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenManager=jwtTokenManager;
    }

    public boolean createCompany(Company company) {
        if (companyRepository.findOptionalByCompanyName(company.getCompanyName()).isPresent()){
            Company savedCompany = save(company);
            return true;
        }else{
            return false;
        }
    }

    public Optional<Company> findByCompanyName(String companyName){
        return companyRepository.findOptionalByCompanyName(companyName);
    }

    public Boolean updateCompany(CompanyUpdateRequestDTO dto) {
        /* Volkan:
            47-48-49-50. satırına şu kontrolü yapmaya gerek kalmadı, JWTTokenManagerda onun hatasını fırlattım. Bütün servislerin decode kısmı uyumlu oldu.
            Yalnız artık tokende hem id hem role var ve tokeni deşifre için şu methodu kullanacağız:
            jwtTokenManager.getClaimsFromToken(token).get(0);     // (get(0) id, get(1) role (admin,guest,supervisor vs) verir, ikisine de ihtiyaç varsa 1 kere methodu çağır, sonucu listede tut gibi...)
            Ayrıca dto.getToken verip deşifre edip companyName çekmişsin hatırlatmak istedim. Tokende sadece id ve role olacak.
            Kod kızıyor diye 51.satıra companyName ekledim optional. Silersin onu :D
         */
//        Optional<String> companyName = jwtTokenManager.decodeToken(dto.getToken());
//        if (companyName.isEmpty()){
//            throw new CompanyServiceException(ErrorType.INVALID_TOKEN);
//        }
        Optional<String> companyName = Optional.of(jwtTokenManager.getClaimsFromToken(dto.getCompanyName()).get(0));
        Optional<Company> optionalByCompanyName = companyRepository.findOptionalByCompanyName(companyName.get());
        if (optionalByCompanyName.isEmpty()){
            throw new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND);
        }
        Company updatedCompany = optionalByCompanyName.get();
        updatedCompany.setCompanyName(dto.getCompanyName());
        updatedCompany.setHolidays(dto.getHolidays());
        updatedCompany.setHrInfos(dto.getHrInfos());
        updatedCompany.setAddress(dto.getAddress());
        updatedCompany.setDepartments(dto.getDepartments());
        updatedCompany.setExpenses(dto.getExpenses());
        updatedCompany.setIncomes(dto.getIncomes());
        updatedCompany.setSupervisorIds(dto.getSupervisorIds());
        update(updatedCompany);
        return true;
    }

    public Boolean softDelete(String companyName) {
        Optional<Company> optionalCompany = findByCompanyName(companyName);
        if (optionalCompany.isPresent()){
            optionalCompany.get().setStatus(false);
            return true;
        }else {
            throw new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND);
        }
    }

    public Boolean hardDelete(String companyName){
        Optional<Company> optionalCompany = findByCompanyName(companyName);
        if (optionalCompany.isPresent()){
            companyRepository.deleteByCompanyName(companyName);
            return true;
        }else {
            throw new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND);
        }
    }
}
