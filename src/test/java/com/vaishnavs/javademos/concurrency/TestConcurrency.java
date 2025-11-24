package com.vaishnavs.javademos.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vaishnavs.javademos.model.PostsEntity;
import com.vaishnavs.javademos.repository.PostsRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
public class TestConcurrency {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PostsRepository postsRepository;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void testPostLikeUpdate10kTimes() throws Exception {
    int postId = 3;

    final int numberOfRequests = 10000;
    final int incrementPerRequest = 1;
    final int expectedFinalLikeCount = numberOfRequests * incrementPerRequest;

    ExecutorService executorService = Executors.newFixedThreadPool(100);
    CountDownLatch latch = new CountDownLatch(numberOfRequests);
    AtomicInteger successCount = new AtomicInteger();
    AtomicInteger failureCount = new AtomicInteger();

    for (int i = 0; i < numberOfRequests; i++) {
      executorService.submit(() -> {
        try {

          mockMvc.perform(put("/posts/" + postId + "/like")).andExpect(status().isOk());
          successCount.incrementAndGet();

        } catch (Exception e) {
          failureCount.incrementAndGet();
          System.err.println("Request failed: " + e.getMessage());
        } finally {
          System.out.println("request called: " + latch.getCount());
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    System.out.println("Successful requests: " + successCount.get());
    System.out.println("Failed requests: " + failureCount.get());

    entityManager.clear();

    PostsEntity post = postsRepository.findById(postId).orElse(null);

    System.out.println("Expected like count: " + expectedFinalLikeCount);
    System.out.println("Actual like count: " + post.getLikesCount());

    assertEquals(expectedFinalLikeCount, post.getLikesCount(), "should be exactly " + expectedFinalLikeCount);
    assertEquals(numberOfRequests, successCount.get(), "succed!");
  }
}

/**
 * package com.vaishnavs.microblogs;
 * 
 * import java.util.concurrent.CountDownLatch;
 * import java.util.concurrent.ExecutorService;
 * import java.util.concurrent.Executors;
 * import java.util.concurrent.atomic.AtomicInteger;
 * 
 * import static org.junit.jupiter.api.Assertions.assertEquals;
 * import static org.junit.jupiter.api.Assertions.assertNotNull;
 * import org.junit.jupiter.api.BeforeEach;
 * import org.junit.jupiter.api.Test;
 * import org.springframework.beans.factory.annotation.Autowired;
 * import
 * org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
 * import org.springframework.boot.test.context.SpringBootTest;
 * import org.springframework.http.MediaType;
 * import org.springframework.test.web.servlet.MockMvc;
 * import static
 * org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
 * import static
 * org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 * 
 * import com.fasterxml.jackson.databind.ObjectMapper;
 * import com.vaishnavs.microblogs.dto.UpdateWalletBalanceDto;
 * import com.vaishnavs.microblogs.model.WalletEntity;
 * import com.vaishnavs.microblogs.repository.WalletRepository;
 * 
 * import jakarta.persistence.EntityManager;
 * 
 * @SpringBootTest
 * @AutoConfigureMockMvc
 *                       public class WalletEntityTest {
 * 
 * @Autowired
 *            private MockMvc mockMvc;
 * 
 * @Autowired
 *            private ObjectMapper objectMapper;
 * 
 * @Autowired
 *            private WalletRepository walletRepository;
 * 
 * @Autowired
 *            private EntityManager entityManager;
 * 
 * @BeforeEach
 *             void setup() {
 *             // Reset wallet with ID 1 to balance 0
 *             WalletEntity wallet =
 *             walletRepository.findById(1L).orElseThrow();
 *             wallet.setBalance(0L);
 *             walletRepository.saveAndFlush(wallet);
 *             }
 * 
 * @Test
 *       public void testWalletBalanceUpdate10000Times() throws Exception {
 *       Long walletId = 1L;
 * 
 *       final int numberOfRequests = 10000;
 *       final long amountPerRequest = 1L;
 *       final long expectedFinalBalance = numberOfRequests * amountPerRequest;
 * 
 *       ExecutorService executorService = Executors.newFixedThreadPool(100);
 *       CountDownLatch latch = new CountDownLatch(numberOfRequests);
 *       AtomicInteger successCount = new AtomicInteger(0);
 *       AtomicInteger failureCount = new AtomicInteger(0);
 * 
 *       for (int i = 0; i < numberOfRequests; i++) {
 *       executorService.submit(() -> {
 *       try {
 *       UpdateWalletBalanceDto dto = new UpdateWalletBalanceDto();
 *       dto.setWalletId(walletId);
 *       dto.setAmount(amountPerRequest);
 * 
 *       mockMvc.perform(put("/wallets/balance")
 *       .contentType(MediaType.APPLICATION_JSON)
 *       .content(objectMapper.writeValueAsString(dto)))
 *       .andExpect(status().isOk());
 *       successCount.incrementAndGet();
 *       } catch (Exception e) {
 *       failureCount.incrementAndGet();
 *       System.err.println("Request failed: " + e.getMessage());
 *       } finally {
 *       latch.countDown();
 *       }
 *       });
 *       }
 * 
 *       latch.await();
 *       executorService.shutdown();
 * 
 *       System.out.println("Successful requests: " + successCount.get());
 *       System.out.println("Failed requests: " + failureCount.get());
 * 
 *       entityManager.clear();
 * 
 *       WalletEntity updatedWallet =
 *       walletRepository.findById(walletId).orElse(null);
 *       assertNotNull(updatedWallet);
 * 
 *       System.out.println("Expected balance: " + expectedFinalBalance);
 *       System.out.println("Actual balance: " + updatedWallet.getBalance());
 * 
 *       assertEquals(expectedFinalBalance, updatedWallet.getBalance(), "Balance
 *       should be exactly " + expectedFinalBalance);
 *       assertEquals(numberOfRequests, successCount.get(), "All requests should
 *       succeed");
 *       }
 *       }
 */